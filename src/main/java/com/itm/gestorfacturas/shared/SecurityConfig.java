package com.itm.gestorfacturas.shared;

import com.itm.gestorfacturas.modules.usuario.Usuario;
import com.itm.gestorfacturas.modules.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Habilita @PreAuthorize("hasRole('ADMIN')") en los controladores
@EnableJpaAuditing(auditorAwareRef = "auditorProvider") // Habilita @CreatedBy y @LastModifiedBy
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsuarioRepository usuarioRepository;

    @Value("${spring.security.oauth2.client.provider.auth0.issuer-uri:https://dev-placeholder.us.auth0.com/}")
    private String issuer;

    @Value("${spring.security.oauth2.client.registration.auth0.client-id:}")
    private String clientId;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos públicos (CSS, JS, imágenes, HTMX)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/error").permitAll()
                // Todo lo demás requiere iniciar sesión con Auth0
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userAuthoritiesMapper(userAuthoritiesMapper())
                )
                .successHandler(primerLoginSuccessHandler())
            )
            .logout(logout -> logout
                .addLogoutHandler(auth0LogoutHandler())
            );

        return http.build();
    }

    /**
     * Puente entre Auth0 y SQL Server:
     * Toma el 'sub' (auth0Id) del token de Auth0, busca el empleado en tu BD local
     * y le asigna el rol (ROLE_ADMIN / ROLE_EMPLEADO) para Spring Security.
     */
    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            for (GrantedAuthority authority : authorities) {
                if (authority instanceof OidcUserAuthority oidcAuth) {
                    String auth0Id = oidcAuth.getIdToken().getSubject();

                    Usuario usuario = usuarioRepository.findByAuth0Id(auth0Id)
                            .orElseThrow(() -> new DisabledException("El usuario de Auth0 no está registrado en el sistema interno."));

                    if (!usuario.isActivo()) {
                        throw new DisabledException("Tu cuenta se encuentra desactivada. Contacta al administrador.");
                    }

                    // Spring Security exige el prefijo "ROLE_" para que funcione hasRole('ADMIN')
                    mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
                }
            }
            return mappedAuthorities;
        };
    }

    /**
     * Interceptor post-login:
     * Si el empleado entra por primera vez con clave temporal, lo redirige
     * obligatoriamente al formulario de cambio de contraseña.
     */
    @Bean
    public AuthenticationSuccessHandler primerLoginSuccessHandler() {
        return (request, response, authentication) -> {
            if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
                String auth0Id = oidcUser.getSubject();

                Optional<Usuario> usuarioOpt = usuarioRepository.findByAuth0Id(auth0Id);
                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    if (usuario.isContrasenaEsTemporal()) {
                        log.info("Primer inicio de sesión detectado para {}. Redirigiendo a cambio de contraseña.", usuario.getCorreo());
                        response.sendRedirect("/usuarios/" + usuario.getIdUsuario() + "/cambiar-contrasena");
                        return;
                    }
                }
            }
            // Si ya cambió su clave temporal, entra al flujo normal del sistema
            response.sendRedirect("/pedidos");
        };
    }

    /**
     * Cierra la sesión tanto en Spring Boot como en el servidor de Auth0
     * para evitar que vuelva a entrar automáticamente por cookie de sesión SSO.
     */
    private LogoutHandler auth0LogoutHandler() {
        return (request, response, authentication) -> {
            try {
                String baseUrl = request.getScheme() + "://" + request.getServerName()
                        + (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort());

                String logoutUrl = UriComponentsBuilder
                        .fromUriString(issuer + "v2/logout")
                        .queryParam("client_id", clientId)
                        .queryParam("returnTo", baseUrl)
                        .build()
                        .toUriString();

                response.sendRedirect(logoutUrl);
            } catch (IOException e) {
                log.error("Error al redirigir al logout de Auth0", e);
            }
        };
    }

    /**
     * Proveedor de auditoría JPA para llenar @CreatedBy y @LastModifiedBy automáticamente.
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof OidcUser oidcUser)) {
                return Optional.of("SISTEMA");
            }
            String email = oidcUser.getEmail();
            return Optional.ofNullable(email != null ? email : oidcUser.getSubject());
        };
    }
}
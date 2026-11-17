package com.itm.gestorfacturas.shared;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class Auth0IdentityFacadeImpl implements IIdentityProviderFacade {
    
    private final RestClient restClient;
    
    @Value("${auth0.domain:dev-placeholder.us.auth0.com}")
    private String domain;

    @Value("${auth0.management.client-id:}")
    private String clientId;

    @Value("${auth0.management.client-secret:}")
    private String clientSecret;

    @Value("${auth0.connection:Username-Password-Authentication}")
    private String connection;

    @Value("${auth0.management.enabled:false}")
    private boolean auth0Enabled;

    public Auth0IdentityFacadeImpl() {
        this.restClient = RestClient.create();
    }

    @Override
    @SuppressWarnings("unchecked")
    public String registrarUsuarioConClaveTemporal(String correo, String nombre, String contrasenaTemporal) {
        if (!auth0Enabled) {
            String mockId = "auth0|local-" + UUID.randomUUID();
            log.warn("[MODO LOCAL - AUTH0 DESACTIVADO] Simulando creación en Auth0 para {} owo. ID asignado: {}", correo, mockId);
            return mockId;
        }

        String token = obtenerManagementToken();

        Map<String, Object> body = new HashMap<>();
        body.put("email", correo.toLowerCase().trim());
        body.put("name", nombre);
        body.put("password", contrasenaTemporal);
        body.put("connection", connection);
        body.put("email_verified", true);
        body.put("app_metadata", Map.of("contrasena_es_temporal", true));

        try {
            Map<String, Object> response = restClient.post()
                    .uri("https://" + domain + "/api/v2/users")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.containsKey("user_id")) {
                return response.get("user_id").toString();
            }
            throw new IllegalArgumentException("Auth0 no retornó un user_id válido OnO");
        } catch (RestClientResponseException erm) {
            log.error("Error de Auth0 al crear usuario UnU ({}): {}", erm.getStatusCode(), erm.getResponseBodyAsString());
            throw new IllegalArgumentException("No se pudo registrar el usuario en Auth0, verifica el formato y los datos ingresados OnO");
        }
    }

    @Override 
    public void actualizarDatosUsuario(String auth0UserId, String nuevoCorreo, String nuevoNombre) {
        if (!auth0Enabled) {
            log.warn("[MODO LOCAL - Auth0 Desactivado] Simulando actualización en Auth0");
            return;
        }

        String token = obtenerManagementToken();

        Map<String, Object> body = new HashMap<>();
        body.put("email", nuevoCorreo.toLowerCase().trim());
        body.put("name", nuevoNombre);
        body.put("connection", connection);

        ejecutarPatchUsuario(auth0UserId, token, body, "actualizar datos del usuario");
    }

    @Override 
    public void actualizarContrasena(String auth0UserId, String nuevaContrasena) {
        if (!auth0Enabled) {
            log.warn("[MODO LOCAL - Auth0 Desactivado] Simulando actualización en Auth0");
            return;
        }

        String token = obtenerManagementToken();

        Map<String, Object> body = new HashMap<>();
        body.put("password", nuevaContrasena);
        body.put("connection", connection);
        body.put("app_metadata", Map.of("contrasena_es_temporal", false));
        
        ejecutarPatchUsuario(auth0UserId, token, body, "cambiar la contraseña");
    }

    @Override
    public void desactivarUsuario(String auth0UserId) {
        if (!auth0Enabled) {
            log.warn("[MODO LOCAL - Auth0 Desactivado] Simulando actualización en Auth0");
            return;
        }

        String token = obtenerManagementToken();

        Map<String, Object> body = Map.of("blocked", true);

        ejecutarPatchUsuario(auth0UserId, token, body, "desactivar/bloquear al usuario UvU");
    }

    private void ejecutarPatchUsuario(String auth0UserId, String token, Map<String, Object> body, String accion) {
        try {
            restClient.patch()
                    .uri("https://{domain}/api/v2/users/{id}", domain, auth0UserId)
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException erm) {
            log.error("Error en Auth0 al {} ({}): {}", accion, erm.getStatusCode(), erm.getResponseBodyAsString());
            throw new IllegalArgumentException("Error al " + accion + " en el proveedor de identidad Auth0 OnO");
        }
    }

    @SuppressWarnings("unchecked")
    private String obtenerManagementToken() {
        Map<String, String> tokenRequest = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "audience", "https://" + domain + "/api/v2/",
                "grant_type", "client_credentials"
        );

        try {
            Map<String, Object> response = restClient.post()
                    .uri("https://" + domain + "/oauth/token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(tokenRequest)
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.containsKey("access_token")) {
                return response.get("access_token").toString();
            }
            throw new IllegalStateException("No se recibió access_token desde Auth0 OnO");
        } catch (RestClientResponseException erm) {
            log.error("Fallo obteniendo Management Token de Auth0: {}", erm.getResponseBodyAsString());
            throw new IllegalStateException("Error de configuración al autenticar en Auth0 Management API OnO");
        }
    }

}

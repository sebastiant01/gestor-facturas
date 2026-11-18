package com.itm.gestorfacturas.modules.usuario;

import com.itm.gestorfacturas.modules.enums.RolUsuario;
import com.itm.gestorfacturas.shared.IIdentityProviderFacade;
import com.itm.gestorfacturas.shared.exception.DatosInvalidos;
import com.itm.gestorfacturas.shared.exception.RecursoDuplicado;
import com.itm.gestorfacturas.shared.exception.RecursoNoEncontrado;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service 
@RequiredArgsConstructor 
public class UsuarioServiceImpl implements IUsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final IIdentityProviderFacade identityProviderFac;

    @Override 
    @Transactional 
    public void guardarUsuario(UsuarioRequestDTO usuarioRequest) {

        if (usuarioRepository.existsByCorreo(usuarioRequest.getCorreo())){
            throw new RecursoDuplicado(
                    "Ya existe un usuario registrado con ese correo OnO: " + usuarioRequest.getCorreo());
        }

        String auth0Id = identityProviderFac.registrarUsuarioConClaveTemporal(
                usuarioRequest.getCorreo(), 
                usuarioRequest.getNombreUsuario(), 
                usuarioRequest.getContrasenaTemporal()
        );

        Usuario usuario = Usuario.builder()
                .nombreUsuario(usuarioRequest.getNombreUsuario())
                .correo(usuarioRequest.getCorreo().toLowerCase().trim())
                .auth0Id(auth0Id)
                .contrasenaEsTemporal(true)
                .rol(usuarioRequest.getRol() != null ? usuarioRequest.getRol() : RolUsuario.EMPLEADO)
                .build();

        usuarioRepository.save(usuario);
    }

    @Override 
    @Transactional(readOnly = true)
    public Usuario consultarUsuarioId(UUID idUsuario) {
        return this.usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado con ID: "
                        + idUsuario));
    }

    @Override 
    @Transactional
    public boolean actualizarUsuario(UUID idUsuario, Usuario usuario) {
        Usuario usuarioExistente = consultarUsuarioId(idUsuario);

        if (!usuarioExistente.getCorreo().equalsIgnoreCase(usuario.getCorreo())
                && usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RecursoDuplicado("El nuevo correo ya está en uso OnO");
        }

        identityProviderFac.actualizarDatosUsuario(
            usuarioExistente.getAuth0Id(),
            usuario.getCorreo(),
            usuario.getNombreUsuario()
        );

        usuarioExistente.setNombreUsuario(usuario.getNombreUsuario());
        usuarioExistente.setCorreo(usuario.getCorreo().toLowerCase().trim());

        usuarioRepository.save(usuarioExistente);
        return true;
    }

    @Override 
    @Transactional
    public boolean cambiarContrasena(UUID idUsuario, String nuevaContrasena) {
        Usuario usuarioExistente = consultarUsuarioId(idUsuario);

        if (nuevaContrasena == null || nuevaContrasena.isBlank() || nuevaContrasena.length() < 12) {
            throw new DatosInvalidos("La nueva contraseña debe tener al menos 12 caracteres OnO");
        }

        identityProviderFac.actualizarContrasena(usuarioExistente.getAuth0Id(), nuevaContrasena);

        usuarioExistente.setContrasenaEsTemporal(false);
        usuarioRepository.save(usuarioExistente);

        return true;
    }

    @Override 
    @Transactional
    public boolean eliminarUsuario(UUID idUsuario) {
        Usuario usuarioExistente = consultarUsuarioId(idUsuario);

        identityProviderFac.desactivarUsuario(usuarioExistente.getAuth0Id());

        usuarioExistente.setActivo(false);
        usuarioRepository.save(usuarioExistente);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario consultarPorAuth0Id(String auth0Id){
        return this.usuarioRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RecursoNoEncontrado("Usuario no encontrado OnO " + auth0Id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosActivos(){
        return this.usuarioRepository.findByActivoTrue();
    }

}

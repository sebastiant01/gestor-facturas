package com.itm.gestorfacturas.modules.usuario;

import java.util.List;
import java.util.UUID;

public interface IUsuarioService {

    void guardarUsuario(UsuarioRequestDTO usuario);

    Usuario consultarUsuarioId(UUID idUsuario);

    Usuario consultarPorAuth0Id(String auth0Id);

    List<Usuario> listarUsuariosActivos();

    boolean actualizarUsuario(UUID idUsuario, Usuario usuario);

    boolean cambiarContrasena(UUID idUsuario, String nuevaContrasena);

    boolean eliminarUsuario(UUID idUsuario);

}

package com.itm.gestorfacturas.modules.usuario;

import java.util.UUID;

public interface IUsuarioService {

    void guardarUsuario(Usuario usuario);

    Usuario consultarUsuarioId(UUID idUsuario);

    boolean actualizarUsuario(UUID idUsuario, Usuario usuario);

    boolean cambiarContrasena(UUID idUsuario, String nuevaContrasena);

    boolean eliminarUsuario(UUID idUsuario);

}

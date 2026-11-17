package com.itm.gestorfacturas.shared;

public interface IIdentityProviderFacade {
    
    String registrarUsuarioConClaveTemporal(String correo, String nombre, String contrasenaTemporal);

    void actualizarDatosUsuario(String auth0UserId, String nuevoCorreo, String nuevoNombre);

    void actualizarContrasena(String auth0UserId, String nuevaContrasena);

    void desactivarUsuario(String auth0UserId);
}

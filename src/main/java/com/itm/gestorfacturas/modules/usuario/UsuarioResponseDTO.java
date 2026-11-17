package com.itm.gestorfacturas.modules.usuario;

import com.itm.gestorfacturas.modules.enums.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private UUID idUsuario;
    private String nombreUsuario;
    private String correo;
    private RolUsuario rol;
    private boolean contrasenaEsTemporal;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private String creadoPor;

    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return UsuarioResponseDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombreUsuario(usuario.getNombreUsuario())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol())
                .contrasenaEsTemporal(usuario.isContrasenaEsTemporal())
                .activo(usuario.isActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .creadoPor(usuario.getCreadoPor())
                .build();
    }
}
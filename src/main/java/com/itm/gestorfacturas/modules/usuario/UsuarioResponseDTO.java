package com.itm.gestorfacturas.modules.usuario;

import com.itm.gestorfacturas.modules.enums.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String contrasenaTemporal;

}

package com.itm.gestorfacturas.modules.usuario;

import com.itm.gestorfacturas.modules.enums.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre del usuario no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres UnU")
    private String nombreUsuario;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe ingresar un formato de correo electrónico válido")
    @Size(max = 150, message = "El correo no puede superar los 150 caracteres UnU")
    private String correo;

    @NotBlank(message = "La contraseña temporal es obligatoria")
    @Size(min = 12, max = 64, message = "La contraseña temporal debe tener entre 8 y 64 caracteres OnO")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*S",
        message = "La contraseña temporal debe incluir al menos una mayúscula, una minúscula y un número (Requisito de Auth0 UwU)"
    )
    private String contrasenaTemporal;

    @Builder.Default
    private RolUsuario rol = RolUsuario.EMPLEADO;

}

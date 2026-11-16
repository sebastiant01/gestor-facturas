package com.itm.gestorfacturas.modules.tipocafe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoCafeRequestDTO {

    @NotBlank(message = "El nombre del tipo de café no puede estar vacío.")
    @Size(min = 3, message = "El tipo del nombre de café debe tener 3 caracteres o más.")
    private String nombreTipoCafe;

    private String descripcionTipoCafe;

}

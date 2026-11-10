package com.itm.gestorfacturas.modules.factura;

import com.itm.gestorfacturas.modules.enums.EstadoFactura;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaRequestDTO {

    @NotNull(message = "Debe asociar un pedido válido para generar la factura")
    private UUID idPedido;

    @NotBlank(message = "El proveedor no puede estar vacío")
    @Builder.Default
    private String proveedor = "Café Premium S.A.";

    @NotNull(message = "La fecha de la factura es obligatoria")
    @Builder.Default
    private LocalDate fecha = LocalDate.now();

    @NotBlank(message = "El nombre del empleado que emite la factura es obligatorio")
    private String nombreEmpleado;

    @Builder.Default
    private EstadoFactura estadoFactura = EstadoFactura.VIGENTE;
}

package com.itm.gestorfacturas.modules.pedido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class PedidoRequestDTO {

    @NotBlank(message = "El nombre del cliente no puede estar vacío")
    private String nombreCliente;

    @NotBlank(message = "El teléfono del cliente no puede estar vacío")
    private String telefonoCliente;

    @NotBlank(message = "La dirección de entrega no puede estar vacía")
    private String direccionEntrega;

    @NotBlank(message = "El país de destino no puede estar vacío")
    private String pais;

    @NotNull(message = "Debe seleccionar un tipo de café válido")
    private UUID idTipoCafe;

    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso en kilogramos debe ser mayor a 0")
    private Double pesokg;

    @Builder.Default
    private LocalDate fechaPedido = LocalDate.now();
}

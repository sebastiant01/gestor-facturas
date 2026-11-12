package com.itm.gestorfacturas.modules.factura;

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
public class FacturaResponseDTO {

    private UUID idFactura;
    private LocalDate fecha;
    private String proveedor;
    private String estadoFactura;
    private String nombreEmpleado;

    private UUID idPedido;
    private String nombreCliente;
    private String direccionEntrega;
    private String pais;
    private String nombreTipoCafe;
    private Double pesokg;

}
package com.itm.gestorfacturas.modules.pedido;

import com.itm.gestorfacturas.modules.enums.EstadoPedido;
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
public class PedidoResponseDTO {

    private UUID idPedido;
    private String nombreCliente;
    private String telefonoCliente;
    private String direccionEntrega;
    private String pais;
    private String nombreTipoCafe;
    private Double pesokg;
    private EstadoPedido estadoPedido;
    private LocalDate fechaPedido;

}

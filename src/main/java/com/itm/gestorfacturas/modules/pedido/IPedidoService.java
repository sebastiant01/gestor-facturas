package com.itm.gestorfacturas.modules.pedido;

import com.itm.gestorfacturas.modules.enums.EstadoPedido;

import java.util.List;
import java.util.UUID;

public interface IPedidoService {

    void guardarPedido(PedidoRequestDTO pedido);

    Pedido consultarPedidoId(UUID idPedido);

    void cambiarEstadoPedido(UUID idPedido);

    List<Pedido> listarPedidos();

    List<Pedido> listarPedidosPorEstado(EstadoPedido estado);
}

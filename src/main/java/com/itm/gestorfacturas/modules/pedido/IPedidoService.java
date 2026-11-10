package com.itm.gestorfacturas.modules.pedido;

import com.itm.gestorfacturas.modules.enums.EstadoPedido;

import java.util.List;
import java.util.UUID;

public interface IPedidoService {

    void guardarPedido(Pedido pedido);

    Pedido consultarPedidoId(UUID idPedido);

    void actualizarPedidoPendiente(UUID idPedido);

    void cambiarEstadoPedido(UUID idPedido, EstadoPedido estado);

    void eliminarPedidoPendiente(UUID idPedido);

    List<Pedido> listarPedidos();

    List<Pedido> listarPedidosPorEstado(EstadoPedido estado);
}

package com.itm.gestorfacturas.modules.pedido;

import com.itm.gestorfacturas.modules.enums.EstadoPedido;
import com.itm.gestorfacturas.modules.tipocafe.TipoCafe;
import com.itm.gestorfacturas.modules.tipocafe.TipoCafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements IPedidoService {

    private final PedidoRepository pedidoRepository;
    private final TipoCafeRepository tipoCafeRepository;

    @Override
    @Transactional
    public void guardarPedido(PedidoRequestDTO pedidoRequest) {
        TipoCafe tipoCafe = tipoCafeRepository.findById(pedidoRequest.getIdTipoCafe());

        Pedido pedido = Pedido.builder()
                .nombreCliente(pedidoRequest.getNombreCliente())
                .telefonoCliente(pedidoRequest.getTelefonoCliente())
                .direccionEntrega(pedidoRequest.getDireccionEntrega())
                .pais(pedidoRequest.getPais())
                .tipoCafe(tipoCafe)
                .pesokg(pedidoRequest.getPesokg())
                .fechaPedido(pedidoRequest.getFechaPedido())
                .build();

        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido consultarPedidoId(UUID idPedido) {
        return this.pedidoRepository.findById(idPedido);
    }

    @Override
    @Transactional
    public void cambiarEstadoPedido(UUID idPedido) {
        Pedido pedido = this.pedidoRepository.findById(idPedido);

        if (pedido.getEstadoPedido() == EstadoPedido.PENDIENTE) {
            pedido.setEstadoPedido(EstadoPedido.FACTURADO);
        } else {
            pedido.setEstadoPedido(EstadoPedido.PENDIENTE);
        }

        this.pedidoRepository.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPedidos() {
        return this.pedidoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosPorEstado(EstadoPedido estado) {
        return this.pedidoRepository.findByEstadoPedido(estado);
    }
}

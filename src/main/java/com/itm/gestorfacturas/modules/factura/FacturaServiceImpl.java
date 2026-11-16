package com.itm.gestorfacturas.modules.factura;

import com.itm.gestorfacturas.modules.enums.EstadoFactura;
import com.itm.gestorfacturas.modules.enums.EstadoPedido;
import com.itm.gestorfacturas.modules.pedido.IPedidoService;
import com.itm.gestorfacturas.modules.pedido.PedidoRepository;
import com.itm.gestorfacturas.modules.pedido.Pedido;
import com.itm.gestorfacturas.shared.exception.RecursoNoEncontrado;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements IFacturaService {

    private final FacturaRepository facturaRepository;
    private final PedidoRepository pedidoRepository;
    private final IPedidoService pedidoService;

    @Override
    @Transactional
    public void guardarFactura(FacturaRequestDTO facturaRequest) {
        Pedido pedido = pedidoRepository.findById(facturaRequest.getIdPedido())
                .orElseThrow(() -> new RecursoNoEncontrado(
                        "Pedido no encontrado con id: " + facturaRequest.getIdPedido()));

        if (pedido.getEstadoPedido() != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException(
                    "El pedido ya fue facturado y no puede volver a facturarse.");
        }

        String nombreEmpleado = SecurityContextHolder.getContext().getAuthentication().getName();

        Factura facturaAGuardar = Factura.builder()
                .pedido(pedido)
                .nombreEmpleado(nombreEmpleado)
                .build();

        facturaRepository.save(facturaAGuardar);

        pedidoService.cambiarEstadoPedido(pedido.getIdPedido());
    }

    @Override
    @Transactional(readOnly = true)
    public Factura consultarFacturaId(UUID idFactura) {
        return facturaRepository.findById(idFactura)
                .orElseThrow(() -> new RecursoNoEncontrado(
                        "Factura no encontrada con id: " + idFactura));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Factura> listarFacturas() {
        return facturaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Factura> buscarPorEmpleado(String nombreEmpleado) {
        return facturaRepository.findByNombreEmpleadoContainingIgnoreCase(nombreEmpleado);
    }

    /**
     * Filtra facturas por estado: VIGENTE o ANULADA.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Factura> buscarPorEstado(EstadoFactura estadoFactura) {
        return facturaRepository.findByEstadoFactura(estadoFactura);
    }
}

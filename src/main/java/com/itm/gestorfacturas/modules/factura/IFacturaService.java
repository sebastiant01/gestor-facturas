package com.itm.gestorfacturas.modules.factura;

import com.itm.gestorfacturas.modules.enums.EstadoFactura;

import java.util.List;
import java.util.UUID;

public interface IFacturaService {

    void guardarFactura(Factura factura);

    Factura consultarFacturaId(UUID idFactura);

    List<Factura> listarFacturas();

    List<Factura> buscarPorEmpleado(String nombreEmpleado);

    List<Factura> buscarPorEstado(EstadoFactura estadoFactura);

}

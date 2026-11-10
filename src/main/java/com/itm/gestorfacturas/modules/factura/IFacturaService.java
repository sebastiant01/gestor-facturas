package com.itm.gestorfacturas.modules.factura;

import java.util.List;
import java.util.UUID;

public interface IFacturaService {

    void guardarFactura(Factura factura);

    Factura consultarFacturaId(UUID idFactura);

    List<Factura> consultarFacturaParametro(String parametro);

    void anularFactura(UUID idFactura);

    List<Factura> listarFacturas();

}

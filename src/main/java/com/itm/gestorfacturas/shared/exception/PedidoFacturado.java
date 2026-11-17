package com.itm.gestorfacturas.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PedidoFacturado extends RuntimeException {

    public PedidoFacturado(String mensaje) { super(mensaje); }
}

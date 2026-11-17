package com.itm.gestorfacturas.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RecursoDuplicado extends RuntimeException {

    public RecursoDuplicado(String mensaje) { super(mensaje); }
}

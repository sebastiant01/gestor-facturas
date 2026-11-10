package com.itm.gestorfacturas.modules.enums;

import lombok.Getter;

@Getter
public enum EstadoPedido {
    PENDIENTE("pendiente"),
    FACTURADO("facturado");

    private final String valor;

    EstadoPedido(String valor) {
        this.valor = valor;
    }
}

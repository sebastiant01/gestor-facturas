package com.itm.gestorfacturas.modules.enums;

import lombok.Getter;

@Getter
public enum EstadoFactura {
    VIGENTE("vigente"),
    ANULADA("anulada");

    private final String valor;

    EstadoFactura(String valor) {
        this.valor = valor;
    }
}

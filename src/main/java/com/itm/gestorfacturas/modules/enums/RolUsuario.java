package com.itm.gestorfacturas.modules.enums;

import lombok.Getter;

@Getter
public enum RolUsuario {
    EMPLEADO("empleado"),
    ADMIN("admin");

    private final String valor;

    RolUsuario(String valor) {
        this.valor = valor;
    }
}

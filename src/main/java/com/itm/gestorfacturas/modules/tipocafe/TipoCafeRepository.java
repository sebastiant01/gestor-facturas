package com.itm.gestorfacturas.modules.tipocafe;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TipoCafeRepository extends JpaRepository<TipoCafe, UUID> {
}

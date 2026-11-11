package com.itm.gestorfacturas.modules.factura;

import com.itm.gestorfacturas.modules.enums.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, UUID> {

    List<Factura> findByEstadoFactura(EstadoFactura estadoFactura);

    List<Factura> findByNombreEmpleadoContainingIgnoreCase(String nombreEmpleado);

}
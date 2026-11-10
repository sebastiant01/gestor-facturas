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

    @Query("SELECT f FROM Factura f WHERE LOWER(f.proveedor) LIKE LOWER(CONCAT('%', :parametro, '%')) " +
            "OR LOWER(CAST(f.estadoFactura AS string)) LIKE LOWER(CONCAT('%', :parametro, '%')) " +
            "OR LOWER(f.nombreEmpleado) LIKE LOWER(CONCAT('%', :parametro, '%')) " +
            "OR LOWER(f.pedido.nombreCliente) LIKE LOWER(CONCAT('%', :parametro, '%'))")
    List<Factura> consultarFacturaParametro(@Param("parametro") String parametro);

    List<Factura> findByEstadoFactura(EstadoFactura estadoFactura);
}
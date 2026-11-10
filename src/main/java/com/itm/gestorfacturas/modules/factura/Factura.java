package com.itm.gestorfacturas.modules.factura;

import com.itm.gestorfacturas.modules.enums.EstadoFactura;
import com.itm.gestorfacturas.modules.pedido.Pedido;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "facturas")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_factura", updatable = false, nullable = false)
    private UUID idFactura;

    @Column(name = "fecha", nullable = false)
    @Builder.Default
    private LocalDate fecha = LocalDate.now();

    @Column(name = "proveedor", nullable = false)
    private String proveedor = "Café Premium S.A.";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido", nullable = false, unique = true)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_factura", nullable = false)
    @Builder.Default
    private EstadoFactura estadoFactura = EstadoFactura.VIGENTE;

    @Column(name = "nombre_empleado", nullable = false)
    private String nombreEmpleado;

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @CreatedBy
    @Column(name = "creado_por", updatable = false)
    private String creadoPor;

    @LastModifiedBy
    @Column(name = "modificado_por")
    private String modificadoPor;

    @PrePersist
    public void prePersist() {
        if (this.fecha == null) {
            this.fecha = LocalDate.now();
        }
        if (this.proveedor == null || this.proveedor.isBlank()) {
            this.proveedor = "Café Premium S.A.";
        }
        if (this.estadoFactura == null) {
            this.estadoFactura = EstadoFactura.VIGENTE;
        }
    }
}

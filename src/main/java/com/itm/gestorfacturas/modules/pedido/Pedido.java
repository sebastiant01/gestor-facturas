package com.itm.gestorfacturas.modules.pedido;

import com.itm.gestorfacturas.modules.enums.EstadoPedido;
import com.itm.gestorfacturas.modules.tipocafe.TipoCafe;
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
@Table(name = "pedidos")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_pedido", updatable = false, nullable = false)
    private UUID idPedido;

    @Column(name = "nombre_cliente", nullable = false)
    private String nombreCliente;

    @Column(name = "telefono_cliente", nullable = false)
    private String telefonoCliente;

    @Column(name = "direccion_entrega", nullable = false)
    private String direccionEntrega;

    @Column(name = "pais", nullable = false)
    private String pais;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_cafe", referencedColumnName = "id_tipo_cafe", nullable = false)
    private TipoCafe tipoCafe;

    @Column(name = "peso_kg", nullable = false)
    private double pesokg;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pedido", nullable = false)
    @Builder.Default
    private EstadoPedido estadoPedido = EstadoPedido.PENDIENTE;

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDate fechaPedido;

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
        if (this.estadoPedido == null) {
            this.estadoPedido = EstadoPedido.PENDIENTE;
        }
        if (this.fechaPedido == null) {
            this.fechaPedido = LocalDate.now();
        }
    }
}
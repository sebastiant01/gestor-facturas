package com.itm.gestorfacturas.modules.tipocafe;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tipos_cafe")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoCafe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_tipo_cafe", updatable = false, nullable = false)
    private UUID idTipoCafe;

    @Column(name = "nombre_tipo_cafe", nullable = false, unique = true)
    private String nombreTipoCafe;

    @Column(name = "descripcion_tipo_cafe")
    private String descripcionTipoCafe;

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

}

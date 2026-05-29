package cl.conaf.herramientas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "asignacion_herramienta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionHerramienta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "herramienta_id", nullable = false)
    private Herramienta herramienta;

    // ID del brigadista que recibe la herramienta
    @Column(name = "brigadista_id", nullable = false)
    private Long brigadistaId;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_devolucion")
    private LocalDateTime fechaDevolucion;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
}

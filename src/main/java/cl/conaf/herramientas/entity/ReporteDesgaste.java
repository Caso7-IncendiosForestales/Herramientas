package cl.conaf.herramientas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reporte_desgaste")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDesgaste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "herramienta_id", nullable = false)
    private Herramienta herramienta;

    @Column(name = "brigadista_id", nullable = false)
    private Long brigadistaId;

    // Nivel de desgaste del 1 al 10
    @Column(name = "nivel_desgaste", nullable = false)
    private Integer nivelDesgaste;

    @Column(name = "descripcion_daño", columnDefinition = "TEXT")
    private String descripcionDaño;

    @Column(name = "fecha_reporte", nullable = false)
    private LocalDateTime fechaReporte;

    @Column(name = "requiere_mantencion")
    private Boolean requiereMantencion;
}
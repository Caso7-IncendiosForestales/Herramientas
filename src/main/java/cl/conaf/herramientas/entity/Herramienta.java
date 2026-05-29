package cl.conaf.herramientas.entity;

import cl.conaf.herramientas.enums.EstadoHerramienta;
import cl.conaf.herramientas.enums.TipoHerramienta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "herramienta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Herramienta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoHerramienta tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoHerramienta estado;

    // Stock disponible para asignar a brigadistas
    @Column(name = "stock_disponible", nullable = false)
    private Integer stockDisponible;

    @Column(name = "stock_total", nullable = false)
    private Integer stockTotal;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "herramienta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AsignacionHerramienta> asignaciones;
}
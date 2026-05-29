package cl.conaf.herramientas.repository;

import cl.conaf.herramientas.entity.Herramienta;
import cl.conaf.herramientas.enums.EstadoHerramienta;
import cl.conaf.herramientas.enums.TipoHerramienta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HerramientaRepository extends JpaRepository<Herramienta, Long> {
    List<Herramienta> findByEstado(EstadoHerramienta estado);
    List<Herramienta> findByTipo(TipoHerramienta tipo);
    List<Herramienta> findByStockDisponibleGreaterThan(Integer stock);
}
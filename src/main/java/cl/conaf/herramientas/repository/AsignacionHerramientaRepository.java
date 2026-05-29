package cl.conaf.herramientas.repository;

import cl.conaf.herramientas.entity.AsignacionHerramienta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsignacionHerramientaRepository 
    extends JpaRepository<AsignacionHerramienta, Long> {
    List<AsignacionHerramienta> findByBrigadistaId(Long brigadistaId);
    List<AsignacionHerramienta> findByHerramientaId(Long herramientaId);
}
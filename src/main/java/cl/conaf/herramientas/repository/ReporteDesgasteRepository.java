package cl.conaf.herramientas.repository;

import cl.conaf.herramientas.entity.ReporteDesgaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReporteDesgasteRepository 
    extends JpaRepository<ReporteDesgaste, Long> {

    List<ReporteDesgaste> findByBrigadistaId(Long brigadistaId);

    // Herramientas con desgaste alto (nivel >= 7)
    @Query("SELECT r FROM ReporteDesgaste r WHERE r.nivelDesgaste >= 7")
    List<ReporteDesgaste> findHerramientasDañadas();
}
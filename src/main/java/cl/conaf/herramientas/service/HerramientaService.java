package cl.conaf.herramientas.service;

import cl.conaf.herramientas.dto.AsignacionDTO;
import cl.conaf.herramientas.dto.HerramientaDTO;
import cl.conaf.herramientas.dto.ReporteDesgasteDTO;
import cl.conaf.herramientas.entity.AsignacionHerramienta;
import cl.conaf.herramientas.entity.Herramienta;
import cl.conaf.herramientas.entity.ReporteDesgaste;
import cl.conaf.herramientas.enums.EstadoHerramienta;
import cl.conaf.herramientas.exception.SinStockException;
import cl.conaf.herramientas.repository.AsignacionHerramientaRepository;
import cl.conaf.herramientas.repository.HerramientaRepository;
import cl.conaf.herramientas.repository.ReporteDesgasteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HerramientaService {

    private final HerramientaRepository herramientaRepository;
    private final AsignacionHerramientaRepository asignacionRepository;
    private final ReporteDesgasteRepository reporteRepository;

    // --- Herramientas ---

    public Herramienta crearHerramienta(HerramientaDTO dto) {
        Herramienta h = new Herramienta();
        h.setNombre(dto.getNombre());
        h.setTipo(dto.getTipo());
        h.setEstado(dto.getEstado());
        h.setStockDisponible(dto.getStockDisponible());
        h.setStockTotal(dto.getStockTotal());
        h.setDescripcion(dto.getDescripcion());
        return herramientaRepository.save(h);
    }

    public List<Herramienta> obtenerTodas() {
        return herramientaRepository.findAll();
    }

    // --- Asignaciones ---

    public AsignacionHerramienta asignarHerramienta(AsignacionDTO dto) {
        Herramienta herramienta = herramientaRepository.findById(dto.getHerramientaId())
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));

        // Si no hay stock lanza la excepción que captura @ControllerAdvice
        if (herramienta.getStockDisponible() <= 0) {
            throw new SinStockException(
                "No hay stock disponible para la herramienta: " + herramienta.getNombre()
            );
        }

        // Descuenta el stock
        herramienta.setStockDisponible(herramienta.getStockDisponible() - 1);
        herramientaRepository.save(herramienta);

        AsignacionHerramienta asignacion = new AsignacionHerramienta();
        asignacion.setHerramienta(herramienta);
        asignacion.setBrigadistaId(dto.getBrigadistaId());
        asignacion.setFechaAsignacion(LocalDateTime.now());
        asignacion.setObservaciones(dto.getObservaciones());

        log.info("Herramienta '{}' asignada al brigadista ID={}",
                herramienta.getNombre(), dto.getBrigadistaId());

        return asignacionRepository.save(asignacion);
    }

    // --- Reporte de desgaste al cierre de turno ---

    public ReporteDesgaste reportarDesgaste(ReporteDesgasteDTO dto) {
        Herramienta herramienta = herramientaRepository.findById(dto.getHerramientaId())
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));

        ReporteDesgaste reporte = new ReporteDesgaste();
        reporte.setHerramienta(herramienta);
        reporte.setBrigadistaId(dto.getBrigadistaId());
        reporte.setNivelDesgaste(dto.getNivelDesgaste());
        reporte.setDescripcionDaño(dto.getDescripcionDaño());
        reporte.setFechaReporte(LocalDateTime.now());

        // Si el desgaste es >= 7 manda a mantención automáticamente
        if (dto.getNivelDesgaste() >= 7) {
            reporte.setRequiereMantencion(true);
            herramienta.setEstado(EstadoHerramienta.EN_MANTENCION);
            herramientaRepository.save(herramienta);
            log.warn("ALERTA: Herramienta '{}' con desgaste alto ({}). Enviada a mantención.",
                    herramienta.getNombre(), dto.getNivelDesgaste());
        } else {
            reporte.setRequiereMantencion(false);
        }

        return reporteRepository.save(reporte);
    }

    public List<ReporteDesgaste> obtenerHerramientasDañadas() {
        return reporteRepository.findHerramientasDañadas();
    }
}
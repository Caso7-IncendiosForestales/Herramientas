package cl.conaf.herramientas.controller;

import cl.conaf.herramientas.dto.AsignacionDTO;
import cl.conaf.herramientas.dto.HerramientaDTO;
import cl.conaf.herramientas.dto.ReporteDesgasteDTO;
import cl.conaf.herramientas.entity.AsignacionHerramienta;
import cl.conaf.herramientas.entity.Herramienta;
import cl.conaf.herramientas.entity.ReporteDesgaste;
import cl.conaf.herramientas.service.HerramientaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/herramientas")
@RequiredArgsConstructor
public class HerramientaController {

    private final HerramientaService herramientaService;

    // --- Herramientas ---
    @PostMapping
    public ResponseEntity<Herramienta> crear(@Valid @RequestBody HerramientaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(herramientaService.crearHerramienta(dto));
    }

    @GetMapping
    public ResponseEntity<List<Herramienta>> obtenerTodas() {
        return ResponseEntity.ok(herramientaService.obtenerTodas());
    }

    // --- Asignaciones ---
    @PostMapping("/asignaciones")
    public ResponseEntity<AsignacionHerramienta> asignar(
            @Valid @RequestBody AsignacionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(herramientaService.asignarHerramienta(dto));
    }

    // --- Reportes de desgaste ---
    @PostMapping("/reportes")
    public ResponseEntity<ReporteDesgaste> reportarDesgaste(
            @Valid @RequestBody ReporteDesgasteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(herramientaService.reportarDesgaste(dto));
    }

    @GetMapping("/reportes/dañadas")
    public ResponseEntity<List<ReporteDesgaste>> obtenerDañadas() {
        return ResponseEntity.ok(herramientaService.obtenerHerramientasDañadas());
    }
}
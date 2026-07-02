package cl.conaf.herramientas.controller;

import cl.conaf.herramientas.dto.AsignacionDTO;
import cl.conaf.herramientas.dto.HerramientaDTO;
import cl.conaf.herramientas.dto.ReporteDesgasteDTO;
import cl.conaf.herramientas.entity.AsignacionHerramienta;
import cl.conaf.herramientas.entity.Herramienta;
import cl.conaf.herramientas.entity.ReporteDesgaste;
import cl.conaf.herramientas.service.HerramientaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/herramientas")
@RequiredArgsConstructor
@Tag(name = "Control de Herramientas",
     description = "Gestión de stock, asignaciones a brigadistas y reportes de desgaste - CONAF")
public class HerramientaController {

    private final HerramientaService herramientaService;

    @Operation(
        summary = "Crear herramienta en inventario",
        description = "Registra una nueva herramienta con su stock disponible y total"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Herramienta creada exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = "Stock negativo rechazado por @Min(0)",
            content = @Content(
                examples = @ExampleObject(
                    value = """
                        {
                          "status": 400,
                          "error": "Error de validación",
                          "message": ["stockDisponible: El stock no puede ser negativo"]
                        }
                        """
                )
            )
        )
    })
    @PostMapping
    public ResponseEntity<Herramienta> crear(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la herramienta",
            required = true,
            content = @Content(
                examples = @ExampleObject(
                    name = "Motosierra",
                    value = """
                        {
                          "nombre": "Motosierra Stihl MS 170",
                          "tipo": "MOTOSIERRA",
                          "estado": "DISPONIBLE",
                          "stockDisponible": 3,
                          "stockTotal": 5,
                          "descripcion": "Para corte de árboles en faenas forestales"
                        }
                        """
                )
            )
        )
        @Valid @RequestBody HerramientaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(herramientaService.crearHerramienta(dto));
    }

    @Operation(
        summary = "Obtener todas las herramientas",
        description = "Lista el inventario completo de herramientas"
    )
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Herramienta>> obtenerTodas() {
        return ResponseEntity.ok(herramientaService.obtenerTodas());
    }

    @Operation(
        summary = "Asignar herramienta a brigadista",
        description = "Asigna una herramienta y descuenta 1 del stock. " +
                      "Si no hay stock lanza SinStockException capturada por @ControllerAdvice"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Herramienta asignada, stock descontado"),
        @ApiResponse(
            responseCode = "400",
            description = "Sin stock disponible",
            content = @Content(
                examples = @ExampleObject(
                    value = """
                        {
                          "status": 400,
                          "error": "Sin stock disponible",
                          "message": "No hay stock disponible para: Motosierra Stihl MS 170"
                        }
                        """
                )
            )
        )
    })
    @PostMapping("/asignaciones")
    public ResponseEntity<AsignacionHerramienta> asignar(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la asignación",
            required = true,
            content = @Content(
                examples = @ExampleObject(
                    name = "Asignación a Felipe",
                    value = """
                        {
                          "herramientaId": 1,
                          "brigadistaId": 42,
                          "observaciones": "Asignada para contención flanco este"
                        }
                        """
                )
            )
        )
        @Valid @RequestBody AsignacionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(herramientaService.asignarHerramienta(dto));
    }

    @Operation(
        summary = "Reportar desgaste al cierre de turno",
        description = "Nivel de desgaste del 1 al 10. Si es >= 7 la herramienta " +
                      "pasa automáticamente a EN_MANTENCION"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reporte registrado exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = "Nivel de desgaste fuera del rango 1-10"
        )
    })
    @PostMapping("/reportes")
    public ResponseEntity<ReporteDesgaste> reportarDesgaste(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del reporte de desgaste",
            required = true,
            content = @Content(
                examples = @ExampleObject(
                    name = "Desgaste alto",
                    value = """
                        {
                          "herramientaId": 1,
                          "brigadistaId": 42,
                          "nivelDesgaste": 8,
                          "descripcionDaño": "Cadena desgastada, requiere reemplazo"
                        }
                        """
                )
            )
        )
        @Valid @RequestBody ReporteDesgasteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(herramientaService.reportarDesgaste(dto));
    }

    @Operation(
        summary = "Ver herramientas dañadas",
        description = "Lista reportes con nivel de desgaste >= 7 " +
                      "para planificar reabastecimiento del siguiente turno"
    )
    @ApiResponse(responseCode = "200", description = "Lista de herramientas dañadas")
    @GetMapping("/reportes/dañadas")
    public ResponseEntity<List<ReporteDesgaste>> obtenerDañadas() {
        return ResponseEntity.ok(herramientaService.obtenerHerramientasDañadas());
    }
}
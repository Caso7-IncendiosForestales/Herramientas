package cl.conaf.herramientas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsignacionDTO {

    @NotNull(message = "La herramienta es obligatoria")
    private Long herramientaId;

    @NotNull(message = "El brigadista es obligatorio")
    private Long brigadistaId;

    private String observaciones;
}
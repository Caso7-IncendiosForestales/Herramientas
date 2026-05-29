package cl.conaf.herramientas.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReporteDesgasteDTO {

    @NotNull(message = "La herramienta es obligatoria")
    private Long herramientaId;

    @NotNull(message = "El brigadista es obligatorio")
    private Long brigadistaId;

    @NotNull(message = "El nivel de desgaste es obligatorio")
    @Min(value = 1, message = "El nivel mínimo es 1")
    @Max(value = 10, message = "El nivel máximo es 10")
    private Integer nivelDesgaste;

    private String descripcionDaño;
}
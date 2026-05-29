package cl.conaf.herramientas.dto;

import cl.conaf.herramientas.enums.EstadoHerramienta;
import cl.conaf.herramientas.enums.TipoHerramienta;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HerramientaDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El tipo es obligatorio")
    private TipoHerramienta tipo;

    @NotNull(message = "El estado es obligatorio")
    private EstadoHerramienta estado;

    @NotNull(message = "El stock disponible es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stockDisponible;

    @NotNull(message = "El stock total es obligatorio")
    @Min(value = 0, message = "El stock total no puede ser negativo")
    private Integer stockTotal;

    private String descripcion;
}
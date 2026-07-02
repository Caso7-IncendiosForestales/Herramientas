package cl.conaf.herramientas;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS-09 Control de Inventario y Herramientas")
                        .version("1.0.0")
                        .description("Microservicio para gestionar stock de herramientas, " +
                                "asignaciones a brigadistas y reportes de desgaste - CONAF")
                        .contact(new Contact()
                                .name("Elizabeth Cabrera")
                                .email("el.cabrera@duocuc.cl")));
    }
}
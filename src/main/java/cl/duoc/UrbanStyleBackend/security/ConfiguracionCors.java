package cl.duoc.UrbanStyleBackend.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class ConfiguracionCors {

    @Bean
    public UrlBasedCorsConfigurationSource fuenteCors() {

        CorsConfiguration configuracion = new CorsConfiguration();

        // Direcciones permitidas del frontend
        configuracion.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "http://localhost:5174"
        ));

        configuracion.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        configuracion.setAllowedHeaders(List.of(
            "Authorization", "Content-Type"
        ));

        UrlBasedCorsConfigurationSource fuente =
                new UrlBasedCorsConfigurationSource();

        fuente.registerCorsConfiguration("/api/**", configuracion);

        return fuente;
    }
}

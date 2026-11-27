package cl.duoc.UrbanStyleBackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.servlet.DispatcherType;

@Configuration 
public class ConfiguracionSeguridad {
    @Bean
    public SecurityFilterChain cadenaSeguridad(HttpSecurity http)
            throws Exception {

        http
            .authorizeHttpRequests(autorizacion -> autorizacion
                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()

                // Catalogo publico
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/productos",
                    "/api/v1/productos"
                ).permitAll()

                // Documentacion Swagger
                .requestMatchers(
                    HttpMethod.GET,
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()

                // Registro publico de usuarios
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/autenticacion/registro"
                ).permitAll()


                // Bloquear el resto temporalmente
                .anyRequest().denyAll()
            )

            .csrf(csrf -> csrf.ignoringRequestMatchers(
                "/api/v1/autenticacion/registro"
            ))

            // Desactivar login predeterminado
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }


    @Bean
    public PasswordEncoder codificadorContrasenas() {
        return new BCryptPasswordEncoder();
    }

}

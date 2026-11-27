package cl.duoc.UrbanStyleBackend.security;

import cl.duoc.UrbanStyleBackend.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;


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
                    "/api/v1/autenticacion/registro",
                    "/api/v1/autenticacion/login"
                ).permitAll()

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/v1/productos/*"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/v1/productos"
                ).hasRole("ADMIN")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/v1/productos/*"
                ).hasRole("ADMIN")


                // Bloquear el resto temporalmente
                .anyRequest().denyAll()
            )

            .csrf(AbstractHttpConfigurer::disable)

            .sessionManagement(sesion -> sesion
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(convertidorJwt())
                )
            )

            // Desactivar login predeterminado
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }


    @Bean
    public JwtAuthenticationConverter convertidorJwt() {

        JwtGrantedAuthoritiesConverter convertidorRoles = new JwtGrantedAuthoritiesConverter();

        convertidorRoles.setAuthoritiesClaimName("rol");

        convertidorRoles.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter convertidor =
                new JwtAuthenticationConverter();

        convertidor.setJwtGrantedAuthoritiesConverter(
            convertidorRoles
        );

        return convertidor;
    }


    @Bean
    public PasswordEncoder codificadorContrasenas() {
        return new BCryptPasswordEncoder();
    }

}

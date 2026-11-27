package cl.duoc.UrbanStyleBackend.security;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class ConfiguracionJwt {

    // Convertir la clave Base64 en una clave criptografica
    private SecretKey obtenerClave(String claveBase64) {

        byte[] bytesClave = Base64.getDecoder()
                .decode(claveBase64);

        if (bytesClave.length < 32) {
            throw new IllegalArgumentException(
                "La clave JWT debe tener al menos 32 bytes"
            );
        }

        return new SecretKeySpec(bytesClave, "HmacSHA256");
    }

    // Firmar los tokens JWT
    @Bean
    public JwtEncoder codificadorJwt(
            @Value("${jwt.secret}") String claveBase64) {

        SecretKey clave = obtenerClave(claveBase64);

        return NimbusJwtEncoder.withSecretKey(clave)
                .algorithm(MacAlgorithm.HS256)
                .build();
    }

    // Verificar los tokens recibidos
    @Bean
    public JwtDecoder decodificadorJwt(
            @Value("${jwt.secret}") String claveBase64) {

        SecretKey clave = obtenerClave(claveBase64);

        return NimbusJwtDecoder.withSecretKey(clave)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}


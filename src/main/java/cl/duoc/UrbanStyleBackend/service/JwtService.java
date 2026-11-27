package cl.duoc.UrbanStyleBackend.service;

import cl.duoc.UrbanStyleBackend.model.Usuario;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;

@Service
public class JwtService {
    private final JwtEncoder codificadorJwt;

    public JwtService(JwtEncoder codificadorJwt) {
        this.codificadorJwt = codificadorJwt;
    }

    public String generarToken(Usuario usuario) {

        Instant ahora = Instant.now();

        Instant expiracion = ahora.plus(Duration.ofHours(1));
        
        JwtClaimsSet datosToken = JwtClaimsSet.builder()
                .issuer("UrbanStyleBackend")
                .issuedAt(ahora)
                .expiresAt(expiracion)
                .subject(usuario.getId())
                .claim("rol", usuario.getRol().name())
                .build();

        JwsHeader encabezado = JwsHeader
                .with(MacAlgorithm.HS256)
                .build();

        
        return codificadorJwt.encode(
                JwtEncoderParameters.from(encabezado, datosToken)
        ).getTokenValue();
    }
}

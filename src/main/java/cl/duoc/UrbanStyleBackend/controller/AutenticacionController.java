package cl.duoc.UrbanStyleBackend.controller;

import cl.duoc.UrbanStyleBackend.model.Usuario;
import cl.duoc.UrbanStyleBackend.service.UsuarioService;
import cl.duoc.UrbanStyleBackend.service.JwtService;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController 
@RequestMapping ("/api/v1/autenticacion")
@Tag (name = "Autenticacion", description = "Registro e inicio de sesion")
public class AutenticacionController {

    private final UsuarioService servicio;
    private final JwtService jwtServicio;

    public AutenticacionController(
            UsuarioService servicio,
            JwtService jwtServicio
        ){
            this.servicio = servicio;
            this.jwtServicio = jwtServicio;
    }

    //Datos que recibimos desde el formulario
    public record DatosRegistrado(
        String nombre,
        String correo,
        String contrasena
    ) {}

    public record DatosLogin(
        String correo,
        String contrasena
    ) {}


    @Operation(
        summary = "Registrar usuario",
        description = "Crea un usuario con rol CLIENTE y contrasena protegida"
    )
    @ApiResponse(responseCode = "201", description = "Usuario registrado")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @ApiResponse(responseCode = "409", description = "Correo ya registrado")
    @PostMapping("/registro")

    public ResponseEntity<Map<String, String>> registrarUsuario(
            @RequestBody DatosRegistrado datos) {

        Usuario usuarioGuardado = servicio.registrarUsuario(
            datos.nombre(),
            datos.correo(),
            datos.contrasena()
        );

        Map<String, String> respuesta = Map.of(
            "id", usuarioGuardado.getId(),
            "nombre", usuarioGuardado.getNomrbe(),
            "correo", usuarioGuardado.getCorreo(),
            "rol", usuarioGuardado.getRol().name()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }


    @Operation(
        summary = "Iniciar sesion",
        description = "Verifica las credenciales y genera un token JWT"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Inicio de sesion correcto"
    )
    @ApiResponse(
        responseCode = "401",
        description = "Credenciales invalidas"
    )
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> iniciarSesion(
            @RequestBody DatosLogin datos) {

        // Autenticar al usuario
        Usuario usuario = servicio.autenticarUsuario(
            datos.correo(),
            datos.contrasena()
        );

        // Generar un token JWT
        String token = jwtServicio.generarToken(usuario);

        // Preparar respuesta
        Map<String, String> respuesta = Map.of(
            "token", token,
            "tipo", "Bearer",
            "rol", usuario.getRol().name()
        );

        return ResponseEntity.ok(respuesta);
    }

}
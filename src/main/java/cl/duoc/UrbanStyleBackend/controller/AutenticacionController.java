package cl.duoc.UrbanStyleBackend.controller;

import cl.duoc.UrbanStyleBackend.model.Usuario;
import cl.duoc.UrbanStyleBackend.service.UsuarioService;

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

    public AutenticacionController(UsuarioService sercio){
        this.servicio = sercio;
    }

    //Datos que recibimos desde el formulario
    public record DatosRegistrado(
        String nombre,
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
}
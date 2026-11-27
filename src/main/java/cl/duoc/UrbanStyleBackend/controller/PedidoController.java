package cl.duoc.UrbanStyleBackend.controller;

import cl.duoc.UrbanStyleBackend.model.Pedido;
import cl.duoc.UrbanStyleBackend.service.PedidoService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Gestion de pedidos de UrbanStyle")
public class PedidoController {

    private final PedidoService servicio;

    public PedidoController(PedidoService servicio) {
        this.servicio = servicio;
    }

    @Operation(
        summary = "Crear pedido",
        description = "Registra un pedido del usuario autenticado"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Pedido creado correctamente"
    )
    @ApiResponse(
        responseCode = "400",
        description = "Datos del pedido invalidos"
    )
    @ApiResponse(
        responseCode = "401",
        description = "Usuario no autenticado"
    )
    @ApiResponse(
        responseCode = "403",
        description = "Usuario sin permisos"
    )
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Pedido solicitud) {

        // Obtener el ID desde el token autenticado
        String usuarioId = jwt.getSubject();

        Pedido pedidoGuardado = servicio.crearPedido(usuarioId, solicitud);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoGuardado);
    }


    @Operation(
        summary = "Listar pedidos",
        description = "Consulta los pedidos registrados"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de pedidos obtenida"
    )
    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {

        return ResponseEntity.ok(
            servicio.listarPedidos()
        );
    }

    
    @Operation(
        summary = "Obtener pedido por ID",
        description = "Consulta los detalles de un pedido"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Pedido encontrado"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Pedido no encontrado"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(
            @PathVariable String id) {

        return ResponseEntity.of(
            servicio.obtenerPedidoPorId(id)
        );
    }

}

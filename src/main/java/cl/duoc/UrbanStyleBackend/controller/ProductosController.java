package cl.duoc.UrbanStyleBackend.controller;

import cl.duoc.UrbanStyleBackend.model.Producto;
import cl.duoc.UrbanStyleBackend.service.ProductoService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController 
@Tag(name = "Productos", description = "Consultas de catalogo UrbanStyle")
public class ProductosController {

    private final ProductoService servicio;

    public ProductosController(ProductoService servicio) {
        this.servicio = servicio;
    }

    @Operation (
        summary = "Listar los productos",
        description = "Devuelve el catalogo completo de UrbanStyle"
    )
    @ApiResponse (
        responseCode = "200",
        description = "Lista de productos obtenida correctamente",
        content = @Content(mediaType = "application/json", 
            array = @ArraySchema(schema = @Schema(implementation = Producto.class))
        )
    )
    @GetMapping("api/productos")
    public List<Producto> obtenerProductos() {
        return servicio.listar();
    }


    @Operation(
        summary = "Crear un producto",
        description = "Registra un nuevo producto"
    )
    @ApiResponse(
        responseCode = "201", //201 = CREATED
        description = "Producto creado correctamente"
    )
    @PostMapping("/api/v1/productos")
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto productoNuevo) {

        Producto productoGuardado = servicio.crearProducto(productoNuevo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productoGuardado);
    }


    @Operation(
    summary = "Actualizar un producto",
    description = "Actualiza los datos de un producto"
    )
    @ApiResponse(
        responseCode = "200", //OK
        description = "Producto actualizado correctamente"
    )
    @ApiResponse(
        responseCode = "404", //NOT FOUND
        description = "Producto no encontrado"
    )
    @PutMapping("/api/v1/productos/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable String id,
            @RequestBody Producto datosActualizado) {

        return ResponseEntity.of(
            servicio.actualizarProducto(id, datosActualizado)
        );
    }


    @Operation(
        summary = "Eliminar un producto",
        description = "Elimina un producto por su identificador"
    )
    @ApiResponse(
        responseCode = "204", // DELETE
        description = "Producto eliminado correctamente"
    )
    @ApiResponse(
        responseCode = "404", //NOT FOUND
        description = "Producto no encontrado"
    )
    @DeleteMapping("/api/v1/productos/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String id) {

        boolean eliminado = servicio.eliminarProducto(id);

        if (eliminado) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}

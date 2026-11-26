package cl.duoc.UrbanStyleBackend.controller;

import cl.duoc.UrbanStyleBackend.model.Producto;
import cl.duoc.UrbanStyleBackend.service.ProductoService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // springdoc incorpora estas anotaciones a la documentación que muestra Swagger UI.
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
        // Spring convierte la lista a JSON en la respuesta HTTP.
        return servicio.listar();
    }

}

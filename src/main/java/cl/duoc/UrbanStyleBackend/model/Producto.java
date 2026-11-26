package cl.duoc.UrbanStyleBackend.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@Document(collection = "productos")
public class Producto {
    @Id
    @Schema(description = "Identificador", example = "1")
    private String id;

    @Schema(description = "Nombre del producto", example = "Polera Nike Essensial")
    private String nombre;

    @Schema(description = "Precio en pesos chilenos", example = "22990")
    private int precio;

    @Schema(description = "Categoria del producto", example = "poleras")
    private String categoria;

    @Schema(description = "Nombre del archivo de imagen", example = "polera1.webp")
    private String imagen;

    @Schema(description = "Indica si el producto es destacado", example = "true")
    private boolean destacado;

    @Schema(description = "Tallas disponibles")
    private List<String> tallas;

    @Schema(description = "Descripcion del producto")
    private String descripcion;
}

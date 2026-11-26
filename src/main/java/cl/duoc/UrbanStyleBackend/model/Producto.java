package cl.duoc.UrbanStyleBackend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter 
@Setter
public class Producto {
    @Schema (description = "Identificador único del producto", example = "1")
    private int id;
    @Schema (description = "Nombre del producto", example = "Polera Nike")
    private String nombre;
    @Schema (description = "URL de la imagen del producto", example = "polera1.webp")
    private String imagen;
    @Schema (description = "Precio del producto en pesos chilenos (CLP)", example = "29990")
    private int precio;

    // Jackson usa este constructor para crear un producto a partir del JSON local.
    @JsonCreator
    public Producto(
        @JsonProperty("id") int id,
        @JsonProperty("nombre") String nombre,
        @JsonProperty("imagen") String imagen,
        @JsonProperty("precio") int precio
    ) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.precio = precio;
    }
}

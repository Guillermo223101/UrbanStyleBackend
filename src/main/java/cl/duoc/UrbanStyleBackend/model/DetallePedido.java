package cl.duoc.UrbanStyleBackend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetallePedido {
    private String productoId;
    private String nombreProducto;
    private String talla;
    private int cantidad;
    private int precioUnitario;
    private int subtotal;
}

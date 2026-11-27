package cl.duoc.UrbanStyleBackend.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "pedidos")
public class Pedido {
    @Id
    private String id;
    private String usuarioId;
    private Instant fechaCreacion;
    private String estado;
    private List<DetallePedido> detalles;
    private int total;
}

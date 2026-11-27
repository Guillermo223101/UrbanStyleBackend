package cl.duoc.UrbanStyleBackend.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import cl.duoc.UrbanStyleBackend.model.DetallePedido;
import cl.duoc.UrbanStyleBackend.model.Pedido;
import cl.duoc.UrbanStyleBackend.model.Producto;
import cl.duoc.UrbanStyleBackend.repository.RepositorioPedidos;
import cl.duoc.UrbanStyleBackend.repository.RepositorioProductos;

@Service
public class PedidoService {

    private final RepositorioPedidos repositorioPedidos;
    private final RepositorioProductos repositorioProductos;

    public PedidoService(
            RepositorioPedidos repositorioPedidos,
            RepositorioProductos repositorioProductos) {

        this.repositorioPedidos = repositorioPedidos;
        this.repositorioProductos = repositorioProductos;
    }

    public Pedido crearPedido(String usuarioId, Pedido solicitud) {

        if (usuarioId == null || usuarioId.isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Usuario no autenticado"
            );
        }

        if (solicitud == null ||
                solicitud.getDetalles() == null ||
                solicitud.getDetalles().isEmpty()) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El pedido debe contener productos"
            );
        }

        if (solicitud.getDetalles().size() > 30) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El pedido supera el limite de productos"
            );
        }

        List<DetallePedido> detallesValidados = new ArrayList<>();

        long total = 0;

        for (DetallePedido solicitado : solicitud.getDetalles()) {

            if (solicitado == null ||
                    solicitado.getProductoId() == null ||
                    solicitado.getProductoId().isBlank()) {

                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Producto invalido"
                );
            }

            Producto producto = repositorioProductos
                .findById(solicitado.getProductoId())
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado"
                    )
                );

            int cantidad = solicitado.getCantidad();

            if (cantidad <= 0 || cantidad > 100) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cantidad invalida"
                );
            }

            if (producto.getPrecio() <= 0) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El producto tiene un precio invalido"
                );
            }

            String talla = null;
            List<String> tallasDisponibles = producto.getTallas();

            if (tallasDisponibles != null &&
                    !tallasDisponibles.isEmpty()) {

                String tallaSolicitada = solicitado.getTalla();

                if (tallaSolicitada == null ||
                        tallaSolicitada.isBlank()) {

                    throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Debes seleccionar una talla"
                    );
                }

                talla = tallasDisponibles.stream()
                    .filter(disponible ->
                        disponible != null &&
                        disponible.equalsIgnoreCase(
                            tallaSolicitada.trim()
                        )
                    )
                    .findFirst()
                    .orElse(null);

                if (talla == null) {
                    throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "La talla seleccionada no existe"
                    );
                }
            }

            long subtotal = (long) producto.getPrecio() * cantidad;

            total += subtotal;

            if (subtotal > Integer.MAX_VALUE || total > Integer.MAX_VALUE) {

                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El monto del pedido supera el limite permitido"
                );
            }

            DetallePedido detalle = new DetallePedido();

            detalle.setProductoId(producto.getId());
            detalle.setNombreProducto(producto.getNombre());
            detalle.setTalla(talla);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal((int) subtotal);

            detallesValidados.add(detalle);
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuarioId);
        pedido.setFechaCreacion(Instant.now());
        pedido.setEstado("PENDIENTE");
        pedido.setDetalles(detallesValidados);
        pedido.setTotal((int) total);

        return repositorioPedidos.save(pedido);
    }


public List<Pedido> listarPedidos() {
    return repositorioPedidos.findAll();
}

public Optional<Pedido> obtenerPedidoPorId(String id) {
    return repositorioPedidos.findById(id);
}

}

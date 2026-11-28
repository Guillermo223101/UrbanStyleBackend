package cl.duoc.UrbanStyleBackend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import cl.duoc.UrbanStyleBackend.model.DetallePedido;
import cl.duoc.UrbanStyleBackend.model.Pedido;
import cl.duoc.UrbanStyleBackend.model.Producto;
import cl.duoc.UrbanStyleBackend.repository.RepositorioPedidos;
import cl.duoc.UrbanStyleBackend.repository.RepositorioProductos;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private RepositorioPedidos repositorioPedidos;

    @Mock
    private RepositorioProductos repositorioProductos;

    @InjectMocks
    private PedidoService servicio;

    private Producto producto;
    private DetallePedido detalle;
    private Pedido solicitud;

    @BeforeEach
    void prepararDatos() {

        producto = new Producto();
        producto.setId("1");
        producto.setNombre("Polera Nike Essential");
        producto.setPrecio(22990);
        producto.setTallas(List.of("S", "M", "L"));

        detalle = new DetallePedido();
        detalle.setProductoId("1");
        detalle.setTalla("M");
        detalle.setCantidad(2);

        solicitud = new Pedido();
        solicitud.setDetalles(List.of(detalle));
    }

    @Test
    void calculaTotalConPrecioRealDelProducto() {

        // Simular la consulta del producto
        when(repositorioProductos.findById("1"))
            .thenReturn(Optional.of(producto));

        // Simular el guardado, sin utilizar MongoDB
        when(repositorioPedidos.save(any(Pedido.class)))
            .thenAnswer(invocacion -> invocacion.getArgument(0));

        // Simular un precio adulterado desde el cliente
        detalle.setPrecioUnitario(100);
        detalle.setSubtotal(200);

        Pedido resultado = servicio.crearPedido(
            "usuario-prueba",
            solicitud
        );

        assertEquals(45980, resultado.getTotal());
        assertEquals("PENDIENTE", resultado.getEstado());

        assertEquals(
            22990,
            resultado.getDetalles().get(0).getPrecioUnitario()
        );

        verify(repositorioPedidos, times(1))
            .save(any(Pedido.class));
    }

    @Test
    void rechazaCantidadNegativa() {

        when(repositorioProductos.findById("1"))
            .thenReturn(Optional.of(producto));

        detalle.setCantidad(-2);

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.crearPedido("usuario-prueba", solicitud)
        );

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());

        verify(repositorioPedidos, never())
            .save(any(Pedido.class));
    }

    @Test
    void rechazaProductoInexistente() {

        detalle.setProductoId("no-existe");

        when(repositorioProductos.findById("no-existe"))
            .thenReturn(Optional.empty());

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.crearPedido("usuario-prueba", solicitud)
        );

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());

        verify(repositorioPedidos, never())
            .save(any(Pedido.class));
    }

    @Test
    void rechazaPedidoVacio() {

        solicitud.setDetalles(List.of());

        ResponseStatusException error = assertThrows(
            ResponseStatusException.class,
            () -> servicio.crearPedido("usuario-prueba", solicitud)
        );

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());

        verify(repositorioPedidos, never())
            .save(any(Pedido.class));
    }
}

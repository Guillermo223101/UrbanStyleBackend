package cl.duoc.UrbanStyleBackend.service;

import cl.duoc.UrbanStyleBackend.model.Producto;
import cl.duoc.UrbanStyleBackend.repository.RepositorioProductos;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service 
public class ProductoService {
    private final RepositorioProductos repositorio;

    public ProductoService(RepositorioProductos repositorio) {
        this.repositorio = repositorio;
    }

    public List<Producto> listar() {
        return repositorio.findAll();
    }

    public Producto crearProducto(Producto productoNuevo){
        productoNuevo.setId(null);
        return repositorio.save(productoNuevo);
    }

    public Optional<Producto> actualizarProducto( //200 || 404 Not found
        String id,
        Producto datosActualizado){
            return repositorio.findById(id)
                .map(productoExistente -> {
                    productoExistente.setNombre(datosActualizado.getNombre());
                    productoExistente.setPrecio(datosActualizado.getPrecio());
                    productoExistente.setCategoria(datosActualizado.getCategoria());
                    productoExistente.setImagen(datosActualizado.getImagen());
                    productoExistente.setDestacado(datosActualizado.isDestacado());
                    productoExistente.setTallas(datosActualizado.getTallas());
                    productoExistente.setDescripcion(datosActualizado.getDescripcion());

                    return repositorio.save(productoExistente);
                });
    }

    public boolean eliminarProducto(String id){
        if (!repositorio.existsById(id)) {
            return false;
        }

        repositorio.deleteById(id);
        return true;
    }
}

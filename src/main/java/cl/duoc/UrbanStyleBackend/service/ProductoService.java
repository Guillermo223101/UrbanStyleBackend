package cl.duoc.UrbanStyleBackend.service;

import cl.duoc.UrbanStyleBackend.model.Producto;
import cl.duoc.UrbanStyleBackend.repository.RepositorioProductos;

import java.util.List;
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
}

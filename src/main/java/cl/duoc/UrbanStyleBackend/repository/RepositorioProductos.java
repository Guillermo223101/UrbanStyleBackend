package cl.duoc.UrbanStyleBackend.repository;

import cl.duoc.UrbanStyleBackend.model.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositorioProductos extends MongoRepository<Producto, String>{

}

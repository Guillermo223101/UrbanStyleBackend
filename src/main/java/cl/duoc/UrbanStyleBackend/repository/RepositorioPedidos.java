package cl.duoc.UrbanStyleBackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import cl.duoc.UrbanStyleBackend.model.Pedido;

public interface RepositorioPedidos extends MongoRepository<Pedido, String>{

}

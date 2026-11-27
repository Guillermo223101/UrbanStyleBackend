package cl.duoc.UrbanStyleBackend.repository;

import cl.duoc.UrbanStyleBackend.model.Usuario;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositorioUsuarios extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByCorreo(String correo);
}

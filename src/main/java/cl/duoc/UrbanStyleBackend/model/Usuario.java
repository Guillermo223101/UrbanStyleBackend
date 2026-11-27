package cl.duoc.UrbanStyleBackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Document (collection = "usuarios")
public class Usuario {
    @Id 
    private String id;

    private String nomrbe;

    @Indexed(unique = true)
    private String correo;

    @JsonIgnore
    private String contrasenaHash;

    private RolUsuario rol;
    private boolean activo = true;
}

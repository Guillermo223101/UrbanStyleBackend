package cl.duoc.UrbanStyleBackend.service;

import cl.duoc.UrbanStyleBackend.model.Usuario;
import cl.duoc.UrbanStyleBackend.model.RolUsuario;
import cl.duoc.UrbanStyleBackend.repository.RepositorioUsuarios;

import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final RepositorioUsuarios repositorioUsuarios;
    private final PasswordEncoder codificadorContrasenas;

    public UsuarioService(
            RepositorioUsuarios repositorioUsuarios,
            PasswordEncoder codificadorContrasenas) {

        this.repositorioUsuarios = repositorioUsuarios;
        this.codificadorContrasenas = codificadorContrasenas;
    }

    public Usuario registrarUsuario(
            String nombre,
            String correo,
            String contrasena) {

        if (nombre == null || nombre.isBlank()
                || correo == null || correo.isBlank()
                || contrasena == null || contrasena.length() < 8) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos de registro invalidos");
        }

        String correoNormalizado = correo.trim().toLowerCase(Locale.ROOT);

        if (repositorioUsuarios.findByCorreo(correoNormalizado).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya se encuentra registrado");
        }

        Usuario usuarioNuevo = new Usuario();

        usuarioNuevo.setNomrbe(nombre.trim());
        usuarioNuevo.setCorreo(correoNormalizado);
        usuarioNuevo.setContrasenaHash(codificadorContrasenas.encode(contrasena));
        usuarioNuevo.setRol(RolUsuario.CLIENTE);
        usuarioNuevo.setActivo(true);

        return repositorioUsuarios.save(usuarioNuevo);
    }
}

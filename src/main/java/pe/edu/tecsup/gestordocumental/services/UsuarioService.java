package pe.edu.tecsup.gestordocumental.services;


import pe.edu.tecsup.gestordocumental.dtos.UsuarioDTO;
import pe.edu.tecsup.gestordocumental.models.Roles;
import pe.edu.tecsup.gestordocumental.models.Usuarios;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    // Actualizar el rol para el user
    void updateUserRole(Long userId, String nombreRol);

    // Listar todos los usuarios
    List<Usuarios> getAllUsuarios();

    // Encontrar un usuario por su ID
    UsuarioDTO getUsuarioById(Long id);

    // Listar todos los roles
    List<Roles> getAllRoles();

    // Buscar un usuario por el correo electronico
    Optional<Usuarios> findBycorreoCorporativo( String correoCorporativo);

    Usuarios registerUsuario(Usuarios usuarios);

}

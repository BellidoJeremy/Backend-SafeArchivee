package pe.edu.tecsup.gestordocumental.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.gestordocumental.dtos.UsuarioDTO;
import pe.edu.tecsup.gestordocumental.models.Roles;
import pe.edu.tecsup.gestordocumental.models.Usuarios;
import pe.edu.tecsup.gestordocumental.models.enums.AppRole;
import pe.edu.tecsup.gestordocumental.repositories.RolesRepository;
import pe.edu.tecsup.gestordocumental.repositories.UsuariosRepository;
import pe.edu.tecsup.gestordocumental.services.UsuarioService;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuariosRepository usuariosRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Override
    public void updateUserRole(Long userId, String nombreRol) {

        Usuarios usuario = usuariosRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("Usuario no encontrado"));

        AppRole appRole = AppRole.valueOf(nombreRol);
        Roles roles = rolesRepository.findBynombreRol(appRole)
                .orElseThrow(() -> new RuntimeException("Role no encontrado"));
        usuario.setRoles(roles);
        usuariosRepository.save(usuario);
    }

    @Override
    public List<Usuarios> getAllUsuarios() {
        return usuariosRepository.findAll();
    }

    @Override
    public UsuarioDTO getUsuarioById(Long id) {
        Usuarios usuarios = usuariosRepository.findById(id).orElseThrow();
        return convertToDTO(usuarios);
    }

    private UsuarioDTO convertToDTO(Usuarios usuario) {
        return new UsuarioDTO(
                usuario.getUserId(),
                usuario.getCorreoCorporativo(),
                usuario.getRoles(),
                usuario.getCreatedDate(),
                usuario.getUpdatedDate()
        );
    }
    @Override
    public List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }

    @Override
    public Optional<Usuarios> findBycorreoCorporativo(String correoCorporativo) {
        return usuariosRepository.findBycorreoCorporativo(correoCorporativo);
    }

    @Override
    public Usuarios registerUsuario(Usuarios usuarios) {
        return usuariosRepository.save(usuarios);
    }

}

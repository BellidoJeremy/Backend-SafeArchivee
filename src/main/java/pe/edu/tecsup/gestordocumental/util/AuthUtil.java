package pe.edu.tecsup.gestordocumental.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pe.edu.tecsup.gestordocumental.models.Usuarios;
import pe.edu.tecsup.gestordocumental.repositories.UsuariosRepository;

@Component
public class AuthUtil {

    @Autowired
    UsuariosRepository usuariosRepository;

    public Long loogedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuarios usuarios = usuariosRepository.findBycorreoCorporativo(authentication.getName())
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        return usuarios.getUserId();
    }

    public Usuarios loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return usuariosRepository.findBycorreoCorporativo(authentication.getName())
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
    }
}

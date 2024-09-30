package pe.edu.tecsup.gestordocumental.webs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.tecsup.gestordocumental.models.Usuarios;
import pe.edu.tecsup.gestordocumental.security.response.UserInfoResponse;
import pe.edu.tecsup.gestordocumental.services.UsuarioService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        Usuarios usuarios =
                usuarioService.findBycorreoCorporativo(userDetails.getUsername()).
                        orElseThrow(() -> new RuntimeException("User not found with email"));

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(
                usuarios.getUserId(),
                usuarios.getCorreoCorporativo(),
                roles
        );
        return ResponseEntity.ok(response);
    }
}

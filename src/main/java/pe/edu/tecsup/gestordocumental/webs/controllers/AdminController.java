package pe.edu.tecsup.gestordocumental.webs.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.tecsup.gestordocumental.dtos.UsuarioDTO;
import pe.edu.tecsup.gestordocumental.models.Roles;
import pe.edu.tecsup.gestordocumental.models.Usuarios;
import pe.edu.tecsup.gestordocumental.services.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    UsuarioService usuarioService;

    // Endpoint para listar usuarios
    @GetMapping("/getusers")
    public ResponseEntity<List<Usuarios>> getAllUsuarios() {
        return new ResponseEntity<>(usuarioService.getAllUsuarios(), HttpStatus.OK);
    }

    // Endpoint para actualizar role
    @PutMapping("/update-role")
    public ResponseEntity<String> updateUsuarioRole(
            @RequestParam Long userId, @RequestParam String nombreRol) {
        usuarioService.updateUserRole(userId,  nombreRol);
        return ResponseEntity.ok("User role updated successfully");
    }

    // Endpoint para listar usuarios por ID
    @GetMapping("/user/{id}")
    public ResponseEntity<UsuarioDTO> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(usuarioService.getUsuarioById(id), HttpStatus.OK);
    }

    // Endpoint para listar los roles
    @GetMapping("/roles")
    public List<Roles> getAllRoles() {
        return usuarioService.getAllRoles();
    }
}

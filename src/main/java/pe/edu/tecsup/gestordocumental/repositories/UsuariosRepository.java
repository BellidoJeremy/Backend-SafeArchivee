package pe.edu.tecsup.gestordocumental.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.tecsup.gestordocumental.models.Usuarios;

import java.util.List;
import java.util.Optional;


public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {

    Optional<Usuarios> findBycorreoCorporativo(String correoCorporativo);

    Boolean existsBycorreoCorporativo(String correoCorporativo);

    List<Usuarios> findByRolesRoleId(int roleId);

}

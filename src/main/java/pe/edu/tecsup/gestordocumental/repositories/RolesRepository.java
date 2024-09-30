package pe.edu.tecsup.gestordocumental.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.tecsup.gestordocumental.models.Roles;
import pe.edu.tecsup.gestordocumental.models.enums.AppRole;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findBynombreRol(AppRole approle);

}

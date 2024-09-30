package pe.edu.tecsup.gestordocumental.repositories;

import pe.edu.tecsup.gestordocumental.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoRepository extends JpaRepository<Documento, Integer> {

}

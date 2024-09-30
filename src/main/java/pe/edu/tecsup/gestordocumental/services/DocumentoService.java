package pe.edu.tecsup.gestordocumental.services;


import pe.edu.tecsup.gestordocumental.exception.DocumentoNotFoundException;
import pe.edu.tecsup.gestordocumental.models.Documento;

public interface DocumentoService {

    /**
     *
     * @param documento
     * @return
     */
    Documento create(Documento documento);

    /**
     *
     * @param id
     * @return
     */
    Documento findById(int id) throws DocumentoNotFoundException;

    /**
     *
     * @param documento
     * @return
     */
    Documento update(Documento documento);

    /**
     *
     * @param id
     * @return
     */
    void delete(Integer id) throws DocumentoNotFoundException;

}

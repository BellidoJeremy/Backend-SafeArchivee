package pe.edu.tecsup.gestordocumental.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.gestordocumental.exception.DocumentoNotFoundException;
import pe.edu.tecsup.gestordocumental.models.Documento;
import pe.edu.tecsup.gestordocumental.repositories.DocumentoRepository;
import pe.edu.tecsup.gestordocumental.services.DocumentoService;

import java.util.Optional;

@Service
@Slf4j
public class DocumentoServiceImpl implements DocumentoService {


    DocumentoRepository documentoRepository;

    public DocumentoServiceImpl (DocumentoRepository documentoRepository)
    {
        this.documentoRepository = documentoRepository;
    }

    // Permite la prueba de creacion de nuevo documento
    /**
     *
     * @param documento
     * @return
     */
    @Override
    public Documento create(Documento documento)
    {
        return documentoRepository.save(documento);
    }

    // Permite la busqueda de un documento por ID
    /**
     *
     * @param id
     * @return
     */
    @Override
    public Documento findById(int id) throws DocumentoNotFoundException
    {
        Optional<Documento> documento = documentoRepository.findById(id);

        if (!documento.isPresent())
            throw new DocumentoNotFoundException("Record not found...!");
        return documento.get();
    }

    // Permite la actualizacion de un nuevo documento
    /**
     *
     * @param documento
     * @return
     */
    @Override
    public Documento update(Documento documento)
    {
        return documentoRepository.save(documento);
    }

    // Permite la eliminacion de un documento por su ID
    /**
     *
     * @param id
     * @return
     */
    @Override
    public void delete(Integer id) throws DocumentoNotFoundException
    {
        Documento documento = findById(id);
        documentoRepository.delete(documento);
    }
}

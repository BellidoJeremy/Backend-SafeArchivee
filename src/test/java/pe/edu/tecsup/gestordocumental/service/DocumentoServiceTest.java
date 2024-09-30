package pe.edu.tecsup.gestordocumental.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pe.edu.tecsup.gestordocumental.exception.DocumentoNotFoundException;
import pe.edu.tecsup.gestordocumental.models.Documento;
import pe.edu.tecsup.gestordocumental.services.DocumentoService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class DocumentoServiceTest {

    @Autowired
    private DocumentoService documentoService;

     /*
        Prueba unitaria de creacion de nuevo documento
     */

    @Test
    public void testCreateDocumento()
    {
        String TITULO = "Gestión de Documentos";
        String AUTOR = "Lucia Gandara, María Dominoti";
        String RESUMEN = "El proyecto se trata de la gestión";
        Integer ANIOPUBLICACION= 2022;
        String ASESOR = "Dr. Carlos Sánchez'";
        String CATEGORIA = "Pretesis";
        String TEMA = "Gestión de documentos electrónicos";
        Boolean ESTADO = true;
        Date CREATED_AT=  new Date();
        Date UPDATE_AT = new Date() ;

        Documento documento = new Documento(TITULO,AUTOR, RESUMEN, ANIOPUBLICACION, ASESOR, CATEGORIA, TEMA, ESTADO, CREATED_AT, UPDATE_AT);
        Documento documentoCreated = this.documentoService.create(documento);

        log.info("DOCUMENTO CREATED :" + documentoCreated.toString());

        assertNotNull(documentoCreated.getId());
        assertEquals(TITULO, documentoCreated.getTitulo());
        assertEquals(AUTOR, documentoCreated.getAutores());
        assertEquals(RESUMEN, documentoCreated.getResumen());
        assertEquals(ANIOPUBLICACION, documentoCreated.getAnioPublicacion());
        assertEquals(ASESOR, documentoCreated.getAsesor());
        assertEquals(CATEGORIA, documentoCreated.getCategoria());
        assertEquals(TEMA, documentoCreated.getTema());
        assertEquals(ESTADO, documentoCreated.getStatus());
        assertEquals(CREATED_AT, documentoCreated.getCreated_at());
        assertEquals(UPDATE_AT, documentoCreated.getUpdated_at());
    }

    /*
        Prueba unitaria de busqueda de documento por su ID
    */
    @Test
    public void testFindDocumentoById()
    {
        int ID = 2;
        String TITULO = "Gestión de documentos electrónicos";
        Documento documento = null;

        try {
            documento = this.documentoService.findById(ID);
        } catch (DocumentoNotFoundException e) {
            fail(e.getMessage());
        }

        log.info("RESULTADO -> " + documento);
        assertEquals(TITULO, documento.getTitulo());
    }

    /*
    Prueba unitaria de modificación de documento
*/
    @Test
    public void testUpdateDocumento() {
        int ID = 4;
        String NUEVO_TITULO = "Prueba documento actualizado";
        Documento documento = null;

        try {
            documento = this.documentoService.findById(ID);
            assertNotNull(documento, "El documento con ID " + ID + " no se encontró para actualizar");

            // Modificar los atributos del documento
            documento.setTitulo(NUEVO_TITULO);

            Documento documentoActualizado = this.documentoService.update(documento);
            assertNotNull(documentoActualizado, "No se pudo actualizar el documento");

            assertEquals(NUEVO_TITULO, documentoActualizado.getTitulo(), "El título del documento no se actualizó correctamente");

        } catch (DocumentoNotFoundException e) {
            fail(e.getMessage());
        }
    }

    /*
        Prueba unitaria de eliminación de documento
    */
    @Test
    public void testDeleteDocumento() {
        int ID = 5;

        try {
            this.documentoService.delete(ID);

            // Intentar buscar el documento eliminado
            Documento documentoEliminado = this.documentoService.findById(ID);

            assertNull(documentoEliminado, "El documento con ID " + ID + " no se eliminó correctamente");

        } catch (DocumentoNotFoundException e) {
            // Si se lanza DocumentoNotFoundException, significa que el documento fue eliminado correctamente
        }
    }

}


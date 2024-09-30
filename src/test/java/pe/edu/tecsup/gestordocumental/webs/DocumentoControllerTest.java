package pe.edu.tecsup.gestordocumental.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pe.edu.tecsup.gestordocumental.models.Documento;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@Slf4j

public class DocumentoControllerTest {

    private static final ObjectMapper obmp = new ObjectMapper();

    // Instancionado mockMVC
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    // Prueba de integacion de crear nuevo documento
    /**
     * @throws Exception
     */
    @Test
    void testCreateDocumento() throws Exception
    {
        // Datos nuevos a ingresar para documento
        String TITULO = "Prueba de mejora";
        String AUTOR = "Rebeca Huallpa, Luz Rodriguez";
        String RESUMEN = "Trata de getsionar los documentos";
        Integer ANIOPUBLICACION= 2022;
        String ASESOR = "Pablo Martinez";
        String CATEGORIA = "Proyecto Integrador";
        String TEMA = "Integracion";


        Documento newDocumento = new Documento();
        newDocumento.setTitulo(TITULO);
        newDocumento.setAutores(AUTOR);
        newDocumento.setResumen(RESUMEN);
        newDocumento.setAnioPublicacion(ANIOPUBLICACION);
        newDocumento.setAsesor(ASESOR);
        newDocumento.setCategoria(CATEGORIA);
        newDocumento.setTema(TEMA);


        mockMvc.perform(post("/api/documentos/create")
                .content(obmp.writeValueAsString(newDocumento))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo", is(TITULO)))
                .andExpect(jsonPath("$.autores", is(AUTOR)))
                .andExpect(jsonPath("$.resumen", is(RESUMEN)))
                .andExpect(jsonPath("$.anioPublicacion", is(ANIOPUBLICACION)))
                .andExpect(jsonPath("$.asesor", is(ASESOR)))
                .andExpect(jsonPath("$.categoria", is(CATEGORIA)))
                .andExpect(jsonPath("$.tema", is(TEMA)));
    }

    // Prueba de integacion listar todos los documentos
    /**
     * @throws Exception
     */
    @Test
    void testFindAllDocumento() throws Exception
    {
        int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/api/documentos/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                //.andExpect(jsonPath("$", hasSize(NRO_RECORD)))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
    }

    // Prueba de integacion busqueda de doc por su ID
    /**
     * @throws Exception
     */
    //@Test
    void testFindDocumentoById() throws Exception {
        String TITULO = "Gestión de documentos electrónicos"; // Actualizado al título del documento modificado en el test anterior

        mockMvc.perform(get("/api/documentos/find/3"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.titulo", is(TITULO))); // Se espera el nuevo título actualizado
    }


    /*
    Prueba de integración para modificar un documento
*/
    @Test
    void testUpdateDocumento() throws Exception {
        // Datos para actualizar el documento
        String nuevoTitulo = "Nuevo Título";
        int documentoId = 3; // Suponiendo que el documento con ID 3 existe en la base de datos

        Documento documentoModificado = new Documento();
        documentoModificado.setId(documentoId);
        documentoModificado.setTitulo(nuevoTitulo);

        mockMvc.perform(put("/api/documentos/update/{id}", documentoId)
                        .content(objectMapper.writeValueAsString(documentoModificado))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Documento actualizado correctamente!"));
    }
    /*
        Prueba de integración para eliminar un documento
    */
    //@Test
    void testDeleteDocumento() throws Exception {
        int documentoId = 6; // ID del documento a eliminar

        mockMvc.perform(delete("/api/documentos/delete/{id}", documentoId))
                .andExpect(status().isOk());

        // Verificar que el documento fue eliminado
        mockMvc.perform(get("/api/documentos/find/{id}", documentoId))
                .andExpect(status().isNotFound());
    }


}

package pe.edu.tecsup.gestordocumental.webs.controllers;

import io.jsonwebtoken.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import pe.edu.tecsup.gestordocumental.models.Documento;
import pe.edu.tecsup.gestordocumental.models.Usuarios;
import pe.edu.tecsup.gestordocumental.repositories.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.tecsup.gestordocumental.services.UsuarioService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

//@CrossOrigin("*")
@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @Autowired
    private DocumentoRepository documentoRepository;


    @Autowired
    private UsuarioService usuarioService;

    // Endpoint de prueba para insertar documento
    /* @PostMapping("/add")
    public Documento add(@RequestBody Documento documento)
    {
        return documentoRepository.save(documento);
    }
    */

    // Endpoint para crear un nuevo documento
    //@PostMapping(value = "/create")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Documento> createDocument(
            @RequestBody Documento documento)
    {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // Buscamos
        Usuarios usuarios = usuarioService.findBycorreoCorporativo(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // establecemos
        documento.setUsuarios(usuarios);

        // Se establece la fecha de creacion
        documento.setCreated_at(new Date());
        // Se establece la fecha de creación
        documento.setUpdated_at(new Date());
        // Se establece el estado por defecto
        documento.setStatus(true);

        Documento documentocreado = documentoRepository.save((documento));

        return ResponseEntity.status(HttpStatus.CREATED).body(documentocreado);
    }

    // Endpoint para listar todos los documentos
    //@GetMapping(value = "/all")
    @GetMapping
    public ResponseEntity<List<Documento>> getAllDocumentos()
    {
        List<Documento> documentos = (List<Documento>) documentoRepository.findAll();
        return ResponseEntity.ok(documentos);
    }

    // Endpoint para obtener un documento por su ID
    @GetMapping(value = "/{id}")
    public ResponseEntity<Documento> getDocumentoById(@PathVariable Integer id)
    {

        Optional<Documento> optionalDocumento = documentoRepository.findById(id);
        return optionalDocumento.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint para actualizar un documento por su ID
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDocumento(@PathVariable Integer id, @RequestBody Documento updateDocumentos)
    {
        Optional<Documento> optionalDocumento = documentoRepository.findById(id);
        if (optionalDocumento.isPresent()) {
            Documento documentos = optionalDocumento.get();
            // Se actualiza los campos del documento con valores nuevos ingresados
            documentos.setTitulo(updateDocumentos.getTitulo());
            documentos.setAutores(updateDocumentos.getAutores());
            documentos.setResumen(updateDocumentos.getResumen());
            documentos.setAnioPublicacion(updateDocumentos.getAnioPublicacion());
            documentos.setAsesor(updateDocumentos.getAsesor());
            documentos.setCategoria(updateDocumentos.getCategoria());
            documentos.setTema(updateDocumentos.getTema());
            documentos.setUpdated_at(new Date()); // Se establece fecha de modificacion
            documentoRepository.save(documentos);
            return ResponseEntity.ok("Documento actualizado correctamente!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento no encontrado!");
    }

    // Endpoint para eliminar un documento
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocumento(@PathVariable Integer id)
    {
        Optional<Documento> optionalDocumento = documentoRepository.findById(id);
        if (optionalDocumento.isPresent())
        {
            documentoRepository.deleteById(id);
            return ResponseEntity.ok("Documento eliminado correctamente!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento no encontrado!");
    }

}

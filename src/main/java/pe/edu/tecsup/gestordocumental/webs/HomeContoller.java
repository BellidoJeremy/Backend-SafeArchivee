package pe.edu.tecsup.gestordocumental.webs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeContoller {
    @GetMapping("/")
    public String home(){
        return "Hola user a la pagina principal";

    }

    @GetMapping("/doc")
    public String doc(){
        return "Listado de doc";

    }
    @GetMapping("/secured")
    public String secured(){
        return "Hola seguridad";
    }
}
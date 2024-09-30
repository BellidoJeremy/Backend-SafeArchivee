package pe.edu.tecsup.gestordocumental.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import pe.edu.tecsup.gestordocumental.models.Roles;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long userId;

    private String correoCorporativo;

    private Roles roles;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}

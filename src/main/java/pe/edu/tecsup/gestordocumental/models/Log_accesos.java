package pe.edu.tecsup.gestordocumental.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "log_accesos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Log_accesos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios id_usuario;

    private Date fecha_ingreso;

    private String direccion_ip;

    private String navegador;

    private String ciudad_acceso;
}

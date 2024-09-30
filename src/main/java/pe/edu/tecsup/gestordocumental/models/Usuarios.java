package pe.edu.tecsup.gestordocumental.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "usuarios")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;


    @Column(name = "correo_corporativo")
    private String correoCorporativo;

    //@Column(name = "rol")
    //private String rol;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @JsonBackReference
    @ToString.Exclude
    private Roles roles;

    // Agregando dos campos de fecha de inicio o fin
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @CreationTimestamp
    private LocalDateTime updatedDate;

    public Usuarios(String correoCorporativo) {
        this.correoCorporativo = correoCorporativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuarios)) return false;
        return userId !=null && userId.equals(((Usuarios) o).getUserId());
    }
}

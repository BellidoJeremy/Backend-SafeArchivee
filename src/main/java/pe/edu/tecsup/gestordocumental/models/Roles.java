package pe.edu.tecsup.gestordocumental.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pe.edu.tecsup.gestordocumental.models.enums.AppRole;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@Data
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "nombre_rol")
    private AppRole nombreRol;

    // Creando relacion de uno a muchos : cada role esta asociado a varios usuarios
    @OneToMany(mappedBy = "roles", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JsonBackReference
    @ToString.Exclude
    private Set<Usuarios> usuariosSet = new HashSet<>();

    // Constructor que solo toma el rol nombre
    public Roles(AppRole nombreRol) {
        this.nombreRol = nombreRol;
    }
}

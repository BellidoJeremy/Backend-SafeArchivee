package pe.edu.tecsup.gestordocumental.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "documento")
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "titulo", length = 100)
    private String titulo;

    @Column(name = "autores", length = 255)
    private String autores;

    @Column(name = "resumen", length = 1000)
    private String resumen;

    @Column(name = "anio_publicacion")
    private Integer anioPublicacion;

    @Column(name = "asesor", length = 100)
    private String asesor;

    @Column(name = "categoria", length = 50)
    private String categoria;

    @Column(name = "tema", length = 100)
    private String tema;

    // Relacion de uno a muchos : muchos documentos a un unico usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuarios;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date created_at;

    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    private Date updated_at;

    public Documento() {
    }

    public Documento(int id, String titulo, String autores, String resumen, Integer anioPublicacion, String asesor, String categoria, String tema, Usuarios usuarios, Boolean status, Date created_at, Date updated_at) {
        this.id = id;
        this.titulo = titulo;
        this.autores = autores;
        this.resumen = resumen;
        this.anioPublicacion = anioPublicacion;
        this.asesor = asesor;
        this.categoria = categoria;
        this.tema = tema;
        this.usuarios = usuarios;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Documento(String titulo, String autores, String resumen, Integer anioPublicacion, String asesor, String categoria, String tema, Usuarios usuarios, Boolean status, Date created_at, Date updated_at) {
        this.titulo = titulo;
        this.autores = autores;
        this.resumen = resumen;
        this.anioPublicacion = anioPublicacion;
        this.asesor = asesor;
        this.categoria = categoria;
        this.tema = tema;
        this.usuarios = usuarios;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    public Documento(String titulo, String autor, String resumen, Integer aniopublicacion, String asesor, String categoria, String tema, Boolean estado, Date createdAt, Date updateAt) {
        this.titulo = titulo;
        this.autores = autor;
        this.resumen = resumen;
        this.anioPublicacion = aniopublicacion;
        this.asesor = asesor;
        this.categoria = categoria;
        this.tema = tema;
        this.created_at = createdAt;
        this.updated_at = updateAt;
    }
}

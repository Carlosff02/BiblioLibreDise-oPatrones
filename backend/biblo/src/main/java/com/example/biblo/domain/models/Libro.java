package com.example.biblo.domain.models;

import com.example.biblo.application.dto.LibroDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "libro")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idlibro;
    @Column(length = 5000)
    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    @Column(length = 5000)
    private String descripcion;

    private String idioma;
    private Integer descargas;
    private String imgSrc;
    private String textHtml;
    private String epub;
    private Long idgutendex;

    @ElementCollection
    @CollectionTable(name = "libro_categorias", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "categoria")
    private List<String> categorias;


    public Libro(LibroDTO libro) {
        this.idgutendex=libro.id();
        this.titulo = libro.titulo();
        this.descargas = libro.descargas();
        this.idioma = libro.idioma() != null && !libro.idioma().isEmpty()
                ? libro.idioma().get(0)
                : "desconocido";
        this.descripcion = (libro.summaries() != null && !libro.summaries().isEmpty())
                ? libro.summaries().get(0)
                : null;
        this.imgSrc = libro.formatos() != null ? libro.formatos().imageJpeg() : null;
        this.textHtml = libro.formatos() != null ? libro.formatos().textHtml() : null;
        this.epub = libro.formatos() != null ? libro.formatos().epub() : null;

        this.categorias = libro.categorias();


        if (libro.autor() != null && !libro.autor().isEmpty()) {
            this.autor = new Autor(libro.autor().get(0));
        }
    }


}

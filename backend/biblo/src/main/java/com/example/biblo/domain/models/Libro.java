package com.example.biblo.domain.models;

import com.example.biblo.application.dto.LibroDTO;
import com.example.biblo.infraestructure.utils.generics.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "libro")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Libro extends BaseEntity<Long> {

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



}

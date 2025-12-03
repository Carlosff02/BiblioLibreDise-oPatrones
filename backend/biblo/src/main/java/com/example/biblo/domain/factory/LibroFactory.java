package com.example.biblo.domain.factory;

import com.example.biblo.application.dto.AutorDTO;
import com.example.biblo.application.dto.LibroDTO;
import com.example.biblo.domain.models.Autor;
import com.example.biblo.domain.models.Libro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


// Aplicación del Patron Factory - Patron Creacional
@RequiredArgsConstructor
@Component
public class LibroFactory {

    private final AutorFactory autorFactory;


    public Libro crearLibroDesdeDTO(LibroDTO dto) {

        Autor autor = null;
        if (dto.autor() != null && !dto.autor().isEmpty()) {
            autor = autorFactory.crearAutorDesdeDTO(dto.autor().get(0));
        }
        // Patron Builder - Patron Creacional
        return Libro.builder()
                .idgutendex(dto.id())
                .titulo(dto.titulo())
                .descargas(dto.descargas())
                .idioma(obtenerIdioma(dto))
                .descripcion(obtenerDescripcion(dto))
                .imgSrc(dto.formatos() != null ? dto.formatos().imageJpeg() : null)
                .textHtml(dto.formatos() != null ? dto.formatos().textHtml() : null)
                .epub(dto.formatos() != null ? dto.formatos().epub() : null)
                .categorias(dto.categorias())
                .autor(autor)
                .build();
    }



    private String obtenerIdioma(LibroDTO dto) {
        return (dto.idioma() != null && !dto.idioma().isEmpty())
                ? dto.idioma().get(0)
                : "desconocido";
    }

    private String obtenerDescripcion(LibroDTO dto) {
        return (dto.summaries() != null && !dto.summaries().isEmpty())
                ? dto.summaries().get(0)
                : null;
    }
}


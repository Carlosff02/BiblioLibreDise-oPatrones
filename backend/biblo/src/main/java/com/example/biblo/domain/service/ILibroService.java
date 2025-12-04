package com.example.biblo.domain.service;

import com.example.biblo.domain.models.Libro;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface ILibroService {

    Page<Libro> buscarPorIdioma(String idioma, int page)
            throws IOException, InterruptedException;

    Page<Libro> buscarLibros(String titulo, String autor, String idioma, int page)
            throws IOException;

    Libro buscarLibro(String titulo)
            throws IOException, InterruptedException;

    List<Libro> buscarLibrosMasPopulares();
}

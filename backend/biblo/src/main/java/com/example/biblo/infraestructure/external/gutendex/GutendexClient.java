package com.example.biblo.infraestructure.external.gutendex;

import com.example.biblo.application.dto.LibroDTO;
import com.example.biblo.application.dto.ResultadoBusquedaDTO;

import java.util.List;

// Aplicación del patrón Adapter - Patron Estructural
public interface GutendexClient {
    List<LibroDTO> buscarLibrosPorIdioma(String idioma, int page);
    ResultadoBusquedaDTO buscar(String titulo, String autor, String idiomaNormalizado, int page);
    LibroDTO buscarPorId(long id);
}

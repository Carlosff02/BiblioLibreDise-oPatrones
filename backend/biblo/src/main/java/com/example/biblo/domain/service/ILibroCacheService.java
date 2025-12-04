package com.example.biblo.domain.service;

import com.example.biblo.domain.models.Libro;
import com.example.biblo.domain.models.PaginasGuardadas;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ILibroCacheService {
    Optional<PaginasGuardadas> obtenerPagina(String idioma, int pagina);
    void guardarPagina(PaginasGuardadas pagina);
}

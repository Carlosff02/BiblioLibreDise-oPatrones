package com.example.biblo.domain.strategy;

import com.example.biblo.application.service.LibroServiceImpl;
import com.example.biblo.domain.models.Libro;
import org.springframework.data.domain.Page;

public interface IStrategyBusqueda {
    Page<Libro> buscar(String titulo, String autor, String idioma, int page, LibroServiceImpl service);
}


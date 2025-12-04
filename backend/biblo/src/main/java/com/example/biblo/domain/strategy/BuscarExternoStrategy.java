package com.example.biblo.domain.strategy;

import com.example.biblo.application.service.LibroServiceImpl;
import com.example.biblo.domain.models.Libro;
import org.springframework.data.domain.Page;

import java.io.IOException;

public class BuscarExternoStrategy implements IStrategyBusqueda {

    @Override
    public Page<Libro> buscar(String titulo, String autor, String idioma, int page, LibroServiceImpl service) {
        try {
            return service.buscarLibros(titulo, autor, idioma, page);
        } catch (IOException e) {
            throw new RuntimeException("Error ejecutando búsqueda externa", e);
        }
    }
}


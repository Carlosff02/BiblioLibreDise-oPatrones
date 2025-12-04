package com.example.biblo.domain.strategy;

import com.example.biblo.application.service.LibroServiceImpl;
import com.example.biblo.domain.models.Libro;
import org.springframework.data.domain.Page;

public class BuscarPorTituloStrategy implements IStrategyBusqueda {

    @Override
    public Page<Libro> buscar(String titulo, String autor, String idioma, int page, LibroServiceImpl service) {
        return service.buscarLibrosEnBd(titulo, null, idioma, idioma, page);
    }
}




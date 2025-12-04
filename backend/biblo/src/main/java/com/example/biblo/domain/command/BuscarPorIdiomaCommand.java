package com.example.biblo.domain.command;

import com.example.biblo.application.service.LibroServiceImpl;
import com.example.biblo.domain.models.Libro;
import org.springframework.data.domain.Page;

public class BuscarPorIdiomaCommand implements BuscarCommand {

    private final String idioma;
    private final int page;
    private final LibroServiceImpl service;

    public BuscarPorIdiomaCommand(String idioma, int page, LibroServiceImpl service) {
        this.idioma = idioma;
        this.page = page;
        this.service = service;
    }

    @Override
    public Page<Libro> ejecutar() throws Exception {
        return service.buscarPorIdioma(idioma, page);
    }
}

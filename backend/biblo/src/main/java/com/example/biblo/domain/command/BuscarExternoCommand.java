package com.example.biblo.domain.command;

import com.example.biblo.application.service.LibroServiceImpl;
import com.example.biblo.domain.models.Libro;
import org.springframework.data.domain.Page;

public class BuscarExternoCommand implements BuscarCommand {

    private final String titulo;
    private final String autor;
    private final String idioma;
    private final int page;
    private final LibroServiceImpl service;

    public BuscarExternoCommand(String titulo, String autor, String idioma, int page, LibroServiceImpl service) {
        this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
        this.page = page;
        this.service = service;
    }

    @Override
    public Page<Libro> ejecutar() throws Exception {
        return service.buscarLibros(titulo, autor, idioma, page);
    }
}

package com.example.biblo.domain.command;

import com.example.biblo.application.service.LibroServiceImpl;
import com.example.biblo.domain.models.Libro;
import org.springframework.data.domain.Page;

public class BuscarPorAutorCommand implements BuscarCommand {

    private final String autor;
    private final int page;
    private final LibroServiceImpl service;

    public BuscarPorAutorCommand(String autor, int page, LibroServiceImpl service) {
        this.autor = autor;
        this.page = page;
        this.service = service;
    }

    @Override
    public Page<Libro> ejecutar() throws Exception {
        return service.buscarLibros(null, autor, null, page);
    }
}

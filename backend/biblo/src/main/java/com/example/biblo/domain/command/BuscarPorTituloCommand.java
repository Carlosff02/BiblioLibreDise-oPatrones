package com.example.biblo.domain.command;
import com.example.biblo.application.service.LibroServiceImpl;
import com.example.biblo.domain.models.Libro;
import org.springframework.data.domain.Page;

public class BuscarPorTituloCommand implements BuscarCommand {

    private final String titulo;
    private final int page;
    private final LibroServiceImpl service;

    public BuscarPorTituloCommand(String titulo, int page, LibroServiceImpl service) {
        this.titulo = titulo;
        this.page = page;
        this.service = service;
    }

    @Override
    public Page<Libro> ejecutar() throws Exception {
        return service.buscarLibros(titulo, null, null, page);
    }
}
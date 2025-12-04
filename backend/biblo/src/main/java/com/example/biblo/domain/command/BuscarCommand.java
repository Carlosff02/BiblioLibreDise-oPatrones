package com.example.biblo.domain.command;

import com.example.biblo.domain.models.Libro;
import com.example.biblo.application.service.LibroServiceImpl;
import org.springframework.data.domain.Page;

public interface BuscarCommand {
    Page<Libro> ejecutar() throws Exception;
}

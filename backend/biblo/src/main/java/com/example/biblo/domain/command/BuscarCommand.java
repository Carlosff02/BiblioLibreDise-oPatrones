package com.example.biblo.domain.command;

import com.example.biblo.domain.models.Libro;
import org.springframework.data.domain.Page;

//Es la interfaz que define el método:
//Page<Libro> ejecutar()
public interface BuscarCommand {
    Page<Libro> ejecutar() throws Exception;
}

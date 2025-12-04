package com.example.biblo.domain.observer;

import com.example.biblo.domain.models.Libro;
import org.springframework.stereotype.Component;


//imprime logs cuando se guarda un libro.
@Component
public class LibroLoggerObserver implements ILibroObserver {

    @Override
    public void onLibroGuardado(Libro libro) {
        System.out.println("📘 [Observer] Libro guardado o actualizado: " + libro.getTitulo());
    }

}


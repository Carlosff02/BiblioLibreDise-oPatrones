package com.example.biblo.domain.observer;

import com.example.biblo.domain.models.Libro;
import org.springframework.stereotype.Component;


//simula envío de email al guardar un libro.
@Component
public class LibroEmailObserver implements ILibroObserver {

    @Override
    public void onLibroGuardado(Libro libro) {
        System.out.println("📧 [EmailObserver] Enviando correo: Se guardó el libro → " + libro.getTitulo());

    }
}

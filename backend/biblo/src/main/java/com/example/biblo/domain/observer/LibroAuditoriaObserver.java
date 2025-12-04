package com.example.biblo.domain.observer;

import com.example.biblo.domain.models.Libro;
import org.springframework.stereotype.Component;

@Component
public class LibroAuditoriaObserver implements ILibroObserver {

    @Override
    public void onLibroGuardado(Libro libro) {
        System.out.println("🗄️ [AuditoriaObserver] Registrando auditoría del libro: " + libro.getTitulo());
    }
}

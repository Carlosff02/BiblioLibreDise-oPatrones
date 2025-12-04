package com.example.biblo.domain.observer;

import com.example.biblo.domain.models.Libro;
public interface ILibroObserver {
    void onLibroGuardado(Libro libro);
}

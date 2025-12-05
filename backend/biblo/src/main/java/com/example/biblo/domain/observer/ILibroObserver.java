package com.example.biblo.domain.observer;

import com.example.biblo.domain.models.Libro;

//Esta interfaz cumple la función del
// método update() del patrón tradicional.

public interface ILibroObserver {
    void onLibroGuardado(Libro libro);
}

package com.example.biblo.domain.observer;

import com.example.biblo.domain.models.Libro;

public interface ILibroObservable {
    void addObserver(ILibroObserver observer);
    void removeObserver(ILibroObserver observer);
    void notifyObservers(Libro libro);
}

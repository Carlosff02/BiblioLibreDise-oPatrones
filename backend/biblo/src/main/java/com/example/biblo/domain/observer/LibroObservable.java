package com.example.biblo.domain.observer;

import com.example.biblo.domain.models.Libro;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


//Mantiene una lista de observadores

@Component
public class LibroObservable implements ILibroObservable {

    private final List<ILibroObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(ILibroObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ILibroObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Libro libro) {
        observers.forEach(obs -> obs.onLibroGuardado(libro));
    }
}


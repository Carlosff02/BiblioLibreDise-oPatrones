package com.example.biblo.infraestructure.config;


import com.example.biblo.domain.observer.ILibroObserver;
import com.example.biblo.domain.observer.LibroObservable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ObserverConfig {

    private final LibroObservable observable;
    private final List<ILibroObserver> observers;

    @PostConstruct
    public void init() {
        // Registrar automáticamente todos los observers @Component
        observers.forEach(observable::addObserver);
    }
}


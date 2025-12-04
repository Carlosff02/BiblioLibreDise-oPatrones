package com.example.biblo.domain.strategy;

import org.springframework.stereotype.Component;

@Component
public class StrategySelector {

    public IStrategyBusqueda seleccionar(String titulo, String autor, String idioma) {

        boolean tieneTitulo = titulo != null && !titulo.isBlank();
        boolean tieneAutor  = autor != null && !autor.isBlank();
        boolean tieneIdioma = idioma != null && !idioma.isBlank();

        if (tieneTitulo && tieneAutor && tieneIdioma) return new BuscarCompletaStrategy();
        if (tieneTitulo && !tieneAutor && !tieneIdioma) return new BuscarPorTituloStrategy();
        if (tieneAutor && !tieneTitulo && !tieneIdioma) return new BuscarPorAutorStrategy();
        if (tieneIdioma && !tieneTitulo && !tieneAutor) return new BuscarPorIdiomaStrategy();

        return new BuscarExternoStrategy(); // caso por defecto
    }
}





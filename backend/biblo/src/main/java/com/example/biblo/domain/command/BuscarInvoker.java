package com.example.biblo.domain.command;

import com.example.biblo.application.service.LibroServiceImpl;

//Es quien recibe la solicitud de búsqueda
// y decide qué comando ejecutar.
public class BuscarInvoker {

    public BuscarCommand build(
            String titulo,
            String autor,
            String idioma,
            int page,
            LibroServiceImpl service) {

        boolean tieneTitulo = titulo != null && !titulo.isBlank();
        boolean tieneAutor = autor != null && !autor.isBlank();
        boolean tieneIdioma = idioma != null && !idioma.isBlank();

        if (tieneTitulo && !tieneAutor && !tieneIdioma)
            return new BuscarPorTituloCommand(titulo, page, service);

        if (!tieneTitulo && tieneAutor && !tieneIdioma)
            return new BuscarPorAutorCommand(autor, page, service);

        if (!tieneTitulo && !tieneAutor && tieneIdioma)
            return new BuscarPorIdiomaCommand(idioma, page, service);

        // Si tiene varios → búsqueda completa
        return new BuscarCompletaCommand(titulo, autor, idioma, page, service);
    }
}

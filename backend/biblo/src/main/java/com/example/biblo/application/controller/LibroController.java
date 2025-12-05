package com.example.biblo.application.controller;

import com.example.biblo.domain.models.Libro;
import com.example.biblo.domain.service.ILibroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LibroController {

    // Principio de Inversion de Dependencias
    private final ILibroService libroService;

    @GetMapping("/buscar-por-nombre")
    public Libro buscarPorNombre(@RequestParam String nombreLibro) throws IOException, InterruptedException {

        return libroService.buscarLibro(nombreLibro);
    }

    @GetMapping("/populares")
    public List<Libro> buscarLibrosPopulares(){
        return libroService.buscarLibrosMasPopulares();
    }

    @GetMapping("/buscar-por-idioma")
    public Page<Libro> buscarLibrosPorIdioma(@RequestParam String idioma, @RequestParam Integer page) throws IOException, InterruptedException {
        return libroService.buscarPorIdioma(idioma, page);
    }

    @GetMapping("/buscar")
    public Page<Libro> buscarLibros(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String idioma,
            @RequestParam(defaultValue = "1") int page) throws IOException, InterruptedException {

        return libroService.buscarLibros(titulo, autor, idioma, page);
    }



}

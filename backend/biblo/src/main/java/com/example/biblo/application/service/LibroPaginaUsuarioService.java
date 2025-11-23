package com.example.biblo.application.service;

import com.example.biblo.domain.models.Libro;
import com.example.biblo.domain.models.PaginaLibroUsuario;
import com.example.biblo.infraestructure.repository.LibroPaginaUsuarioRepository;
import com.example.biblo.infraestructure.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibroPaginaUsuarioService {

    private final LibroPaginaUsuarioRepository repository;
    private final LibroRepository libroRepository;

    public String buscarPaginaPorUsuarioYLibro(String titulo, Long usuarioId){
        Optional<Libro> libro = libroRepository.findByTitulo(titulo);
        if(libro.isPresent()){
            String paginaLibroUsuario = repository.buscarPaginaPorUsuarioIdYLibroId(libro.get().getIdlibro(),usuarioId);
            return paginaLibroUsuario;
        }
        return null;
    }
    public void guardarPaginaPorUsuarioYLibro(String titulo, Long usuarioId,String cfi){
        Optional<Libro> libro = libroRepository.findByTitulo(titulo);
        System.out.println("Libro");
        System.out.println(libro.isPresent());
        if(libro.isPresent()){
            Optional<PaginaLibroUsuario> paginaLibroUsuarioBuscar = repository.buscarPorUsuarioIdAndLibroId(libro.get().getIdlibro(),usuarioId);
            System.out.println(paginaLibroUsuarioBuscar.isPresent());
            if(paginaLibroUsuarioBuscar.isPresent()){
                paginaLibroUsuarioBuscar.get().setCfi(cfi);
                repository.save(paginaLibroUsuarioBuscar.get());
            } else {
                PaginaLibroUsuario paginaLibroUsuario = new PaginaLibroUsuario(null, usuarioId, libro.get().getIdlibro(), cfi);
                System.out.println("guardar");
                repository.save(paginaLibroUsuario);
            }
        }
    }


}

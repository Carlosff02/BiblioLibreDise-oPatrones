package com.example.biblo.application.service;

import com.example.biblo.domain.models.Libro;
import com.example.biblo.domain.models.PaginasGuardadas;
import com.example.biblo.domain.service.ILibroCacheService;
import com.example.biblo.infraestructure.repository.PaginasGuardadasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibroCacheServiceImpl implements ILibroCacheService {
    private final PaginasGuardadasRepository paginasGuardadasRepository;

    @Override
    public Optional<PaginasGuardadas> obtenerPagina(String idioma, int pagina) {
        return paginasGuardadasRepository.findByIdiomaAndNumeroPagina(idioma, pagina);
    }

    @Override
    public void guardarPagina(PaginasGuardadas pagina) {
        paginasGuardadasRepository.save(pagina);
    }
}

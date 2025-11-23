package com.example.biblo.infraestructure.repository;

import com.example.biblo.domain.models.PaginasGuardadas;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaginasGuardadasRepository extends JpaRepository<PaginasGuardadas, Long> {
    @EntityGraph(attributePaths = {
            "libros",
            "libros.autor"
    })
    Optional<PaginasGuardadas> findByIdiomaAndNumeroPagina(String idioma, Integer numeroPagina);

    @Query(value = """
            SELECT p.*
                FROM paginas_guardadas p
                WHERE p.idioma = :idioma
                AND p.numero_pagina = :page
    """, nativeQuery = true)
    Optional<PaginasGuardadas> findByIdiomaAndNumeroPaginaConLibrosYAutores(
            @Param("idioma") String idioma,
            @Param("page") Integer page);


}

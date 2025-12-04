package com.example.biblo.infraestructure.repository;

import com.example.biblo.domain.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTitulo(String titulo);

    @Query(value = """
            SELECT * FROM public.libro WHERE epub_local_path IS NULL
            ORDER BY descargas DESC LIMIT 10
            """, nativeQuery = true)
    List<Libro> buscarLibrosPopulares();

    Optional<Libro> findFirstByTituloContainingIgnoreCase(String titulo);



    @Query("""
        SELECT DISTINCT l FROM Libro l
        LEFT JOIN FETCH l.autor a
        WHERE LOWER(l.idioma) = LOWER(:idioma) 
    """)
    Optional<List<Libro>> buscarPorIdioma(@Param("idioma") String idioma);


    @Query(value = """
        SELECT DISTINCT l FROM Libro l
        LEFT JOIN FETCH l.autor a
        WHERE LOWER(l.idioma) = LOWER(:idioma)
          AND LOWER(a.nombre) LIKE LOWER(CONCAT('%', :autor, '%'))
    """)
    Optional<List<Libro>> buscarPorIdiomaYAutor(
            @Param("idioma") String idioma,
            @Param("autor") String autor);


    @Query("""
            SELECT DISTINCT l FROM Libro l
            LEFT JOIN FETCH l.autor a
            WHERE (LOWER(l.idioma) = LOWER(:idioma) OR :idioma = 'todos')
              AND (
                   LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))
                   OR LOWER(a.nombre) LIKE LOWER(CONCAT('%', :autor, '%'))
              )
            
    """)
    Optional<List<Libro>> buscarPorIdiomaYTituloOAutor(
            @Param("idioma") String idioma,
            @Param("titulo") String titulo,
            @Param("autor") String autor);



    @Query("""
        SELECT DISTINCT l FROM Libro l
        LEFT JOIN FETCH l.autor a
        WHERE LOWER(l.idioma) = LOWER(:idioma)
          AND LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))
          AND LOWER(a.nombre) LIKE LOWER(CONCAT('%', :autor, '%'))
    """)
    Optional<List<Libro>> buscarPorIdiomaAutorYTitulo(
            @Param("idioma") String idioma,
            @Param("titulo") String titulo,
            @Param("autor") String autor);
}

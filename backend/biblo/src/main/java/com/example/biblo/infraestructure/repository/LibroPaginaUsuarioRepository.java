package com.example.biblo.infraestructure.repository;


import com.example.biblo.domain.models.PaginaLibroUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LibroPaginaUsuarioRepository extends JpaRepository<PaginaLibroUsuario, Long> {
    @Query(value = """
            SELECT cfi
                            FROM pagina_libro_usuario
                            WHERE libro_id = :idLibro AND usuario_id = :usuarioId
                            LIMIT 1;
            """, nativeQuery = true)
    String buscarPaginaPorUsuarioIdYLibroId(Long idLibro, Long usuarioId);

    @Query(value = """
            SELECT * FROM pagina_libro_usuario
            WHERE libro_id=:idLibro
            AND usuario_id=:usuarioId
            """, nativeQuery = true)
    Optional<PaginaLibroUsuario> buscarPorUsuarioIdAndLibroId(Long idLibro, Long usuarioId);
}

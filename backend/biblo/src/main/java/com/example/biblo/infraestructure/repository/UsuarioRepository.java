package com.example.biblo.infraestructure.repository;

import com.example.biblo.domain.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByUserIdOrEmail(String userId, String email);
}

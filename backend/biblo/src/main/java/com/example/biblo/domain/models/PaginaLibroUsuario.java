package com.example.biblo.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pagina_libro_usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginaLibroUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long usuarioId;
    private Long libroId;
    private String cfi;
}

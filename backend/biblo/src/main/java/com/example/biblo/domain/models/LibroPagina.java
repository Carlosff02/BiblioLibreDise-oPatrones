package com.example.biblo.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="libro_pagina")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LibroPagina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="libro_id")
    private Libro libro;
    @ManyToOne()
    @JoinColumn(name = "pagina_id")
    @JsonIgnore
    private PaginasGuardadas paginaGuardada;
}

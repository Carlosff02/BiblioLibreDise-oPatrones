package com.example.biblo.domain.strutural;

import lombok.Getter;
import lombok.Setter;

/**
 * PATRÓN BRIDGE - Abstraccion
 *
 * Representa un campo de libro que puede ser normalizado.
 * Mantiene una referencia al normalizador
 */
public class CampoLibro {

    @Getter
    private final String valor;
    @Setter
    private INormalizadorTexto normalizador; // ← ESTE ES EL BRIDGE

    public CampoLibro(String valor, INormalizadorTexto normalizador) {
        this.valor = valor;
        this.normalizador = normalizador;
    }

    /**
     * Normaliza el campo usando el normalizador actual
     */
    public String normalizar() {
        return normalizador.normalizar(valor);
    }

}

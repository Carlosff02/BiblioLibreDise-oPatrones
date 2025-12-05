package com.example.biblo.domain.strutural;

/**
 * PATRÓN BRIDGE - Implementador
 * Define las operaciones de normalización que pueden variar
 */

public interface INormalizadorTexto {
    String normalizar(String texto);
}

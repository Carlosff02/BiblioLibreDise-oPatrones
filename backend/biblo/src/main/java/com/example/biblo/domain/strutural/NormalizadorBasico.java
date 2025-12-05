package com.example.biblo.domain.strutural;

import org.springframework.stereotype.Component;

/**
 * PATRÓN BRIDGE - CONCRETE IMPLEMENTOR 1
 * Implementación básica: trim + lowercase
 */
@Component
public class NormalizadorBasico implements INormalizadorTexto {

    @Override
    public String normalizar(String texto) {
        if (texto == null) {
            System.out.println("🔵 [NormalizadorBasico] Texto null recibido, retornando vacío");
            return "";
        }

        String original = texto;
        String normalizado = texto.trim().toLowerCase();

        System.out.println("🔵 [NormalizadorBasico] Normalizando:");
        System.out.println("   Original:    '" + original + "'");
        System.out.println("   Normalizado: '" + normalizado + "'");

        return normalizado;
    }
}
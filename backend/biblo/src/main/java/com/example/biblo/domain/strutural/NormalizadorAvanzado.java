package com.example.biblo.domain.strutural;

import org.springframework.stereotype.Component;
import java.text.Normalizer;

/**
 * PATRÓN BRIDGE - CONCRETE IMPLEMENTOR 2
 * Implementación avanzada: elimina acentos
 */
@Component
public class NormalizadorAvanzado implements INormalizadorTexto {

    @Override
    public String normalizar(String texto) {
        if (texto == null || texto.isBlank()) {
            System.out.println("🟣 [NormalizadorAvanzado] Texto vacío recibido");
            return "";
        }

        String original = texto;

        // Elimina acentos: "García" -> "Garcia"
        String normalizado = Normalizer.normalize(texto.toLowerCase().trim(), Normalizer.Form.NFD);
        normalizado = normalizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        System.out.println("   Normalizando (eliminando acentos):");
        System.out.println("   Original:    '" + original + "'");
        System.out.println("   Normalizado: '" + normalizado + "'");

        return normalizado;
    }
}

package com.example.biblo.domain.factory;

// Patron Factory
public class NormalizerFactory {
    public static Normalizer createNormalizer(String tipo) {
        if (tipo.equalsIgnoreCase("texto")) {
            return new TextNormalizer();
        } else if (tipo.equalsIgnoreCase("idioma")) {
            return new IdiomaNormalizer();
        } else {
            throw new
                    IllegalArgumentException("Tipo de normalizador desconocido");
        }
    }
}

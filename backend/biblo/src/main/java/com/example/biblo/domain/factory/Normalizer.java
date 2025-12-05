package com.example.biblo.domain.factory;

// Principio SRP
// Principio Inversión de Dependencias
public interface Normalizer {
    String normalize(String value);
}

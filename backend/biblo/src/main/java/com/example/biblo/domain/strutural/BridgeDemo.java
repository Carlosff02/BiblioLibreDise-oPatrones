package com.example.biblo.domain.strutural;

import com.example.biblo.domain.models.Libro;
import org.springframework.stereotype.Component;

@Component
public class BridgeDemo {

    private final LibroBridgeService bridgeService;

    public BridgeDemo(LibroBridgeService bridgeService) {
        this.bridgeService = bridgeService;
    }

    public void ejecutarPruebas() {
        System.out.println("\n========================================");
        System.out.println("   PATRÓN BRIDGE - PRUEBAS");
        System.out.println("========================================\n");

        // Libro de prueba
        Libro libro = Libro.builder()
                .titulo("  García Márquez: Cien Años  ")
                .idioma("Español")
                .build();

        System.out.println("Título original: '" + libro.getTitulo() + "'\n");

        // Prueba 1: Normalización básica
        Libro libroBasico = bridgeService.normalizarBasico(libro);
        System.out.println("Básico: '" + libroBasico.getTitulo() + "'\n");

        // Prueba 2: Normalización avanzada
        Libro libroAvanzado = bridgeService.normalizarAvanzado(libro);
        System.out.println("Avanzado: '" + libroAvanzado.getTitulo() + "'\n");

        // Prueba 3: Flexibilidad
        String resultado = bridgeService.demostrarFlexibilidad("José García");
        System.out.println(resultado + "\n");

        System.out.println("========================================");
        System.out.println("   PRUEBAS COMPLETADAS");
        System.out.println("========================================\n");
    }
}
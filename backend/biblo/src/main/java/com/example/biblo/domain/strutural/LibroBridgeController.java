package com.example.biblo.domain.strutural;


import com.example.biblo.domain.models.Libro;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * CONTROLADOR DE DEMOSTRACIÓN DEL PATRÓN BRIDGE
 */
@RestController
@RequestMapping("/api/bridge")
public class LibroBridgeController {

    private final LibroBridgeService bridgeService;

    public LibroBridgeController(LibroBridgeService bridgeService) {
        this.bridgeService = bridgeService;
    }

    /**
     * DEMO 1: Normalización básica
     * POST /api/bridge/basico
     */
    @PostMapping("/basico")
    public ResponseEntity<Libro> normalizarBasico(@RequestBody Libro libro) {
        return ResponseEntity.ok(bridgeService.normalizarBasico(libro));
    }

    /**
     * DEMO 2: Normalización avanzada
     * POST /api/bridge/avanzado
     */
    @PostMapping("/avanzado")
    public ResponseEntity<Libro> normalizarAvanzado(@RequestBody Libro libro) {
        return ResponseEntity.ok(bridgeService.normalizarAvanzado(libro));
    }

    @GetMapping("/demo")
    public ResponseEntity<Map<String, String>> demostrarBridge(@RequestParam String texto) {
        String resultado = bridgeService.demostrarFlexibilidad(texto);

        Map<String, String> response = new HashMap<>();
        response.put("original", texto);
        response.put("resultado", resultado);

        return ResponseEntity.ok(response);
    }
}

package com.example.biblo.application.service;

import com.example.biblo.domain.service.Normalizer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IdiomaNormalizer implements Normalizer {
    // Principio Open Closed
    private static final Map<String, String> MAPA = Map.ofEntries(
            // Español
            Map.entry("español", "es"),
            Map.entry("es", "es"),
            Map.entry("castellano", "es"),

            // Inglés
            Map.entry("english", "en"),
            Map.entry("ingles", "en"),
            Map.entry("en", "en"),

            // Francés
            Map.entry("french", "fr"),
            Map.entry("francés", "fr"),
            Map.entry("fr", "fr"),

            // Alemán
            Map.entry("deutsch", "de"),
            Map.entry("aleman", "de"),
            Map.entry("de", "de"),

            // Italiano
            Map.entry("italian", "it"),
            Map.entry("italiano", "it"),
            Map.entry("it", "it"),

            // Portugués
            Map.entry("portuguese", "pt"),
            Map.entry("portugues", "pt"),
            Map.entry("pt", "pt"),

            // Neerlandés / Holandés
            Map.entry("dutch", "nl"),
            Map.entry("neerlandes", "nl"),
            Map.entry("holandes", "nl"),
            Map.entry("nl", "nl"),

            // Danés
            Map.entry("danish", "da"),
            Map.entry("danes", "da"),
            Map.entry("da", "da"),

            // Sueco
            Map.entry("swedish", "sv"),
            Map.entry("sueco", "sv"),
            Map.entry("sv", "sv"),

            // Noruego
            Map.entry("norwegian", "no"),
            Map.entry("noruego", "no"),
            Map.entry("no", "no"),

            // Finés
            Map.entry("finnish", "fi"),
            Map.entry("finlandes", "fi"),
            Map.entry("fi", "fi"),

            // Griego
            Map.entry("greek", "el"),
            Map.entry("griego", "el"),
            Map.entry("el", "el"),

            // Latín
            Map.entry("latin", "la"),
            Map.entry("la", "la"),

            // Polaco
            Map.entry("polish", "pl"),
            Map.entry("polaco", "pl"),
            Map.entry("pl", "pl"),

            // Ruso
            Map.entry("russian", "ru"),
            Map.entry("ruso", "ru"),
            Map.entry("ru", "ru"),

            // Chino
            Map.entry("chinese", "zh"),
            Map.entry("chino", "zh"),
            Map.entry("zh", "zh"),

            // Japonés
            Map.entry("japanese", "ja"),
            Map.entry("japones", "ja"),
            Map.entry("ja", "ja"),

            // Árabe
            Map.entry("arabic", "ar"),
            Map.entry("arabe", "ar"),
            Map.entry("ar", "ar"),

            // Húngaro
            Map.entry("hungarian", "hu"),
            Map.entry("hungaro", "hu"),
            Map.entry("hu", "hu"),

            // Checo
            Map.entry("czech", "cs"),
            Map.entry("checo", "cs"),
            Map.entry("cs", "cs")
    );

    @Override
    public String normalize(String value) {
        if (value == null || value.isBlank()) return "";
        String normalized = value.toLowerCase().trim();
        return MAPA.getOrDefault(normalized, normalized);
    }
}

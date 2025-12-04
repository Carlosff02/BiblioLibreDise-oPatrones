package com.example.biblo.application.service;

import com.example.biblo.domain.service.ITraduccionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TraduccionServiceImpl implements ITraduccionService {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String traducir(String texto, String source, String target) {

        if (texto == null || texto.isBlank()) return texto;

        try {

            Map<String, String> payload = new HashMap<>();
            payload.put("q", texto);
            payload.put("source", source);
            payload.put("target", target);
            payload.put("format", "text");

            String jsonPayload = mapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5000/translate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .timeout(Duration.ofSeconds(30))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error HTTP " + response.statusCode() + " => " + response.body());
            }

            JsonNode root = mapper.readTree(response.body());
            return root.path("translatedText").asText();

        } catch (Exception e) {
            System.out.println("⚠️ Error traduciendo texto: " + e.getMessage());
            return texto;
        }
    }
}

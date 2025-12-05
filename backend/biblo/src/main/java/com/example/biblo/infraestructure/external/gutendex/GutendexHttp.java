package com.example.biblo.infraestructure.external.gutendex;

import com.example.biblo.application.dto.LibroDTO;
import com.example.biblo.application.dto.ResultadoBusquedaDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

// Aplicación del patrón Adapter - Patron Estructural
@Component
public class GutendexHttp implements GutendexClient{

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();


    @Override
    public ResultadoBusquedaDTO buscar(String titulo, String autor, String idiomaNormalizado, int page) {

        try {
            String baseUrl = "https://gutendex.com/books/";
            StringBuilder url = new StringBuilder(baseUrl);
            url.append("?page=").append(page);


            if (idiomaNormalizado != null && !idiomaNormalizado.isEmpty() && !idiomaNormalizado.equals("todos")) {
                url.append("&languages=").append(idiomaNormalizado);
            }


            String tituloQ = titulo != null ? titulo.trim().toLowerCase() : "";
            String autorQ = autor != null ? autor.trim().toLowerCase() : "";

            StringBuilder search = new StringBuilder();

            if (!tituloQ.isEmpty()) search.append(tituloQ);
            if (!autorQ.isEmpty()) {
                if (search.length() > 0) search.append(" ");
                search.append(autorQ);
            }

            if (search.length() > 0) {
                url.append("&search=").append(
                        URLEncoder.encode(search.toString(), StandardCharsets.UTF_8)
                );
            }

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url.toString()))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());
            long total = root.path("count").asLong(0);
            JsonNode results = root.get("results");

            List<LibroDTO> libros =
                    Arrays.asList(mapper.treeToValue(results, LibroDTO[].class));

            return new ResultadoBusquedaDTO(total, libros, url.toString());

        } catch (Exception e) {
            throw new RuntimeException("Error consultando Gutendex", e);
        }
    }


}

package com.example.biblo.application.controller;

import com.example.biblo.application.dto.ArchivoResponseDTO;
import com.example.biblo.application.service.LibroPaginaUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/visor-epub")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EpubProxyController {
    private final LibroPaginaUsuarioService service;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${epub.cache.dir:epubs-cache}")
    private String epubCacheDir;

    @GetMapping("/epub")
    public ResponseEntity<ArchivoResponseDTO> proxyEpub(
            @RequestParam String url,
            @RequestParam String titulo,
            @RequestParam Optional<Long> usuarioId
    ) {
        try {
            Path epubDir = Paths.get(epubCacheDir);
            if (!Files.exists(epubDir)) {
                Files.createDirectories(epubDir);
            }

            String pagina = null;
            String fileName = url.replaceAll("[^a-zA-Z0-9.-]", "_") + ".epub";
            Path epubPath = epubDir.resolve(fileName);

            byte[] epubBytes;

            if(usuarioId.isPresent()){
            pagina = service.buscarPaginaPorUsuarioYLibro(titulo, usuarioId.get());
            };

            if (Files.exists(epubPath)) {
                epubBytes = Files.readAllBytes(epubPath);
            } else {
                epubBytes = restTemplate.getForObject(url, byte[].class);

                if (epubBytes == null || epubBytes.length == 0) {
                    throw new IOException("No se pudo descargar el archivo desde la URL.");
                }

                Files.write(epubPath, epubBytes, StandardOpenOption.CREATE);
            }

            ArchivoResponseDTO dto = new ArchivoResponseDTO(epubBytes, pagina);

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ArchivoResponseDTO(
                            ("Error al descargar o leer el EPUB: " + e.getMessage()).getBytes(),
                            null
                    ));
        }
    }
    @GetMapping()
    public ResponseEntity guardarPaginaLibroUsuario(@RequestParam String titulo, @RequestParam Long usuarioId,@RequestParam String cfi){
        service.guardarPaginaPorUsuarioYLibro(titulo,usuarioId,cfi);
        return ResponseEntity.ok(Map.of("Guardado correctamente","Ok"));
    }


}

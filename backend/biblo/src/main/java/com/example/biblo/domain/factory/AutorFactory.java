package com.example.biblo.domain.factory;

import com.example.biblo.application.dto.AutorDTO;
import com.example.biblo.domain.models.Autor;
import org.springframework.stereotype.Component;

// Patron Factory - Patron Creacional
@Component
public class AutorFactory {
    public Autor crearAutorDesdeDTO(AutorDTO dto) {
        if (dto == null) return null;
        // Patron Builder - Patron Creacional
        return Autor.builder()
                .nombre(dto.nombre())
                .fechanacimiento(dto.fechaNacimiento())
                .fechafallecimiento(dto.fechaFallecimiento())
                .build();
    }
}

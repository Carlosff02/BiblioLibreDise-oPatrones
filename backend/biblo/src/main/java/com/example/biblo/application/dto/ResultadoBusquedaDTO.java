package com.example.biblo.application.dto;

import java.util.List;

public record ResultadoBusquedaDTO(
        long total,
        List<LibroDTO> resultados,
        String urlConsultada
) {}


package com.example.biblo.application.dto;

public record PaginaLibroAutorDto(

        // 📄 paginas_guardadas (p)
        Long idpagina,
        String idioma,
        Integer numero_pagina,
        Integer total_registros,
        String fecha_consulta,        // cámbialo a LocalDateTime si es necesario
        String fuente,

        // 🔗 libro_pagina (lp)
        Long id_libro_pagina,
        Long pagina_id,
        Long libro_id,

        // 📚 libro (l)
        Long idlibro,
        String titulo,
        String idiomaLibro,          // ALIAS NECESARIO
        Integer descargas,
        Long autor_idLibro,          // ALIAS NECESARIO
        String img_src,
        String text_html,
        String epub,
        String descripcion,
        Long idgutendex,

        // 👤 autor (a)
        Long autor_id,
        String nombre,
        String fechanacimiento,
        String fechafallecimiento

) {}


package com.example.biblo.domain.strutural;


import com.example.biblo.domain.models.Libro;
import org.springframework.stereotype.Service;

/**
 * SERVICIO QUE USA EL PATRÓN BRIDGE
 *
 * Demuestra cómo diferentes campos pueden usar diferentes normalizadores
 */
@Service
public class LibroBridgeService {

    private final INormalizadorTexto normalizadorBasico;
    private final INormalizadorTexto normalizadorAvanzado;

    public LibroBridgeService(NormalizadorBasico basico, NormalizadorAvanzado avanzado) {
        this.normalizadorBasico = basico;
        this.normalizadorAvanzado = avanzado;
    }

    /**
     * Normaliza un libro usando estrategia BÁSICA
     */
    public Libro normalizarBasico(Libro libro) {
        System.out.println("🌉 [Bridge] Usando normalización BÁSICA");

        CampoLibro titulo = new CampoLibro(libro.getTitulo(), normalizadorBasico);

        return Libro.builder()
                .titulo(titulo.normalizar())
                .autor(libro.getAutor())
                .descripcion(libro.getDescripcion())
                .idioma(libro.getIdioma())
                .descargas(libro.getDescargas())
                .imgSrc(libro.getImgSrc())
                .textHtml(libro.getTextHtml())
                .epub(libro.getEpub())
                .idgutendex(libro.getIdgutendex())
                .categorias(libro.getCategorias())
                .build();
    }

    /**
     * Normaliza un libro usando estrategia AVANZADA
     */
    public Libro normalizarAvanzado(Libro libro) {
        System.out.println("🌉 [Bridge] Usando normalización AVANZADA");

        CampoLibro titulo = new CampoLibro(libro.getTitulo(), normalizadorAvanzado);

        return Libro.builder()
                .titulo(titulo.normalizar())
                .autor(libro.getAutor())
                .descripcion(libro.getDescripcion())
                .idioma(libro.getIdioma())
                .descargas(libro.getDescargas())
                .imgSrc(libro.getImgSrc())
                .textHtml(libro.getTextHtml())
                .epub(libro.getEpub())
                .idgutendex(libro.getIdgutendex())
                .categorias(libro.getCategorias())
                .build();
    }

    /**
     * DEMUESTRA LA FLEXIBILIDAD DEL BRIDGE:
     * Cambia el normalizador en runtime
     */
    public String demostrarFlexibilidad(String texto) {
        System.out.println("\n🌉 [Bridge] Demostrando cambio de estrategia en runtime");

        CampoLibro campo = new CampoLibro(texto, normalizadorBasico);
        String resultadoBasico = campo.normalizar();

        System.out.println("🔄 Cambiando a estrategia avanzada...");
        campo.setNormalizador(normalizadorAvanzado);
        String resultadoAvanzado = campo.normalizar();

        return String.format("Básico: '%s' | Avanzado: '%s'", resultadoBasico, resultadoAvanzado);
    }
}

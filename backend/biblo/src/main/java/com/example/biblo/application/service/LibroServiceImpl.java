package com.example.biblo.application.service;

import com.example.biblo.application.dto.AutorDTO;
import com.example.biblo.application.dto.LibroDTO;
import com.example.biblo.application.dto.ResultadoBusquedaDTO;
import com.example.biblo.domain.command.BuscarCommand;
import com.example.biblo.domain.command.BuscarInvoker;
import com.example.biblo.domain.factory.AutorFactory;
import com.example.biblo.domain.factory.LibroFactory;
import com.example.biblo.domain.models.Autor;
import com.example.biblo.domain.models.Libro;
import com.example.biblo.domain.models.LibroPagina;
import com.example.biblo.domain.models.PaginasGuardadas;
import com.example.biblo.domain.observer.LibroObservable;
import com.example.biblo.domain.service.ILibroCacheService;
import com.example.biblo.domain.service.ILibroService;
import com.example.biblo.domain.service.ITraduccionService;
import com.example.biblo.domain.service.Normalizer;
import com.example.biblo.domain.strategy.IStrategyBusqueda;
import com.example.biblo.domain.strategy.StrategySelector;
import com.example.biblo.infraestructure.external.gutendex.GutendexClient;
import com.example.biblo.infraestructure.repository.AutorRepository;
import com.example.biblo.infraestructure.repository.LibroRepository;
import com.example.biblo.infraestructure.repository.PaginasGuardadasRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibroServiceImpl implements ILibroService {

    // Principio de Inversión de Dependencias
    private final GutendexClient gutendexClient;
    private final ILibroCacheService libroCacheService;
    private final ITraduccionService traduccionService;

    private final LibroFactory libroFactory;
    private final AutorFactory autorFactory;
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final PaginasGuardadasRepository paginasGuardadasRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final Normalizer textoNormalizer = new TextNormalizer();
    private final Normalizer idiomaNormalizer = new IdiomaNormalizer();
    private final LibroObservable observable;

    private final StrategySelector strategySelector;


    @Transactional
    public Page<Libro> buscarPorIdioma(String idioma, int page)
            throws IOException, InterruptedException {

        String idiomaNormalizado = normalizarIdioma(idioma);
        if (idiomaNormalizado == null) return Page.empty();

        int pageSize = 32;

        Optional<PaginasGuardadas> paginaGuardada =
                paginasGuardadasRepository.findByIdiomaAndNumeroPagina(idiomaNormalizado, page);

        if (paginaGuardada.isPresent()) {
            return construirPaginaDesdeCache(paginaGuardada.get(), pageSize);
        }

        ResultadoBusquedaDTO resultado = gutendexClient.buscar(
                null, null, idiomaNormalizado, page
        );

        if (resultado.resultados().isEmpty()) {
            return Page.empty();
        }

        List<Libro> libros = procesarLibros(resultado.resultados());
        libroRepository.saveAll(libros);

        guardarPagina(resultado, libros, idiomaNormalizado, page);

        return new PageImpl<>(libros, PageRequest.of(page - 1, pageSize), resultado.total());
    }

    private String normalizarIdioma(String idioma) {
        return switch (idioma.toLowerCase()) {
            case "español" -> "es";
            case "english" -> "en";
            default -> null;
        };
    }

    private Page<Libro> construirPaginaDesdeCache(PaginasGuardadas pagina, int pageSize) {

        List<Libro> libros = pagina.getLibroPaginas()
                .stream()
                .map(LibroPagina::getLibro)
                .toList();

        return new PageImpl<>(
                libros,
                PageRequest.of(pagina.getNumeroPagina() - 1, pageSize),
                pagina.getTotalRegistros()
        );
    }

    private List<Libro> procesarLibros(List<LibroDTO> dtos) {

        List<Libro> libros = new ArrayList<>();

        for (LibroDTO dto : dtos) {

            Libro libro = libroFactory.crearLibroDesdeDTO(dto);

            Autor autor = procesarAutor(dto);
            libro.setAutor(autor);

            libros.add(libro);
        }

        return libros;
    }

    private Autor procesarAutor(LibroDTO dto) {

        if (dto.autor() == null || dto.autor().isEmpty()) return null;

        Autor autor = autorFactory.crearAutorDesdeDTO(dto.autor().get(0));

        String[] partes = autor.getNombre().split(",");
        if (partes.length == 2) {
            autor.setNombre(partes[1].trim() + " " + partes[0].trim());
        }

        Autor finalAutor = autor;

        return autorRepository.findByNombre(autor.getNombre())
                .orElseGet(() -> autorRepository.save(finalAutor));
    }

    private void guardarPagina(ResultadoBusquedaDTO resultado, List<Libro> libros,
                               String idioma, int page) {

        List<LibroPagina> libroPaginas = libros.stream()
                .map(libro -> new LibroPagina(null, libro, null))
                .toList();

        PaginasGuardadas pagina = new PaginasGuardadas(
                null,
                idioma,
                page,
                resultado.total(),
                LocalDateTime.now(),
                resultado.urlConsultada(),
                libroPaginas
        );

        paginasGuardadasRepository.save(pagina);
    }


    @Transactional
    public Libro buscarLibro(String titulo) throws IOException, InterruptedException {
        Optional<Libro> libroBuscar1 = libroRepository.findFirstByTituloContainingIgnoreCase(titulo);
        System.out.println(libroBuscar1.isPresent());

        if (libroBuscar1.isPresent()) {
            Libro libroExistente = libroBuscar1.get();


            if ((libroExistente.getDescripcion() == null || libroExistente.getDescripcion().isBlank())) {

                System.out.println("🔍 Libro con datos incompletos. Buscando información en Gutendex...");


                Libro datosGutendex = obtenerLibroDesdeGutendex(libroExistente.getIdgutendex());

                if (datosGutendex != null) {


                    if (libroExistente.getDescripcion() == null || libroExistente.getDescripcion().isBlank()) {
                        if (datosGutendex.getDescripcion() != null && !datosGutendex.getDescripcion().isBlank()) {
                            String descripcionTraducida = traducirADescripcionEspanol(datosGutendex.getDescripcion(), "auto");
                            libroExistente.setDescripcion(descripcionTraducida);
                            System.out.println("✅ Descripción actualizada para: " + libroExistente.getTitulo());
                        } else {
                            System.out.println("⚠️ No se encontró descripción en Gutendex.");
                        }
                    }


                    if (libroExistente.getCategorias() == null || libroExistente.getCategorias().isEmpty()) {
                        if (datosGutendex.getCategorias() != null && !datosGutendex.getCategorias().isEmpty()) {
                            List<String> categoriasTraducidas = libroExistente.getCategorias().stream()
                                    .filter(Objects::nonNull)
                                    .map(cat -> traducirADescripcionEspanol(cat, "auto"))
                                    .collect(Collectors.toList());
                            libroExistente.setCategorias(categoriasTraducidas);
                            System.out.println("✅ Categorías actualizadas para: " + libroExistente.getTitulo());
                        } else {
                            System.out.println("⚠️ No se encontraron categorías en Gutendex.");
                        }
                    }


                    libroRepository.save(libroExistente);
                } else {
                    System.out.println("⚠️ No se encontró información en Gutendex para: " + titulo);
                }
            }

            return libroExistente;
        }


        String urlStr = "https://gutendex.com/books/?search=" + URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlStr))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();

        if (json == null || json.isEmpty()) {
            System.out.println("⚠️ No se encontraron libros en Gutendex.");
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        JsonNode resultsNode = root.path("results");

        if (!resultsNode.isArray() || resultsNode.isEmpty()) {
            System.out.println("⚠️ No se encontraron resultados en Gutendex.");
            return null;
        }


        JsonNode primerLibro = resultsNode.get(0);
        LibroDTO datos = mapper.treeToValue(primerLibro, LibroDTO.class);
        Libro libro = libroFactory.crearLibroDesdeDTO(datos);


        if (datos.summaries() != null && !datos.summaries().isEmpty()) {
            String descripcionTraducida = traducirADescripcionEspanol(datos.summaries().get(0), datos.idioma().isEmpty() ? "auto" : datos.idioma().get(0));
            libro.setDescripcion(descripcionTraducida);
        }


        if (datos.autor() != null && !datos.autor().isEmpty()) {
            AutorDTO autorDTO = datos.autor().get(0);
            Autor autor = autorFactory.crearAutorDesdeDTO(autorDTO);

            String nombreOriginal = autor.getNombre();
            String[] partes = nombreOriginal.split(",");
            if (partes.length == 2) {
                autor.setNombre(partes[1].trim() + " " + partes[0].trim());
            }

            Autor autorEntity = autorRepository.findByNombre(autor.getNombre())
                    .orElseGet(() -> autorRepository.save(autor));

            libro.setAutor(autorEntity);
        }


        if (datos.categorias() != null && !datos.categorias().isEmpty()) {
            List<String> categoriasTraducidas = datos.categorias().stream()
                    .filter(Objects::nonNull)
                    .map(cat -> traducirADescripcionEspanol(cat, "auto"))
                    .collect(Collectors.toList());
            libro.setCategorias(categoriasTraducidas);
        }




        libroRepository.save(libro);
        observable.notifyObservers(libro);

        System.out.println("💾 Libro guardado: " + libro.getTitulo());

        return libro;
    }

    private Libro obtenerLibroDesdeGutendex(Long id) {
        try {
            String url = "https://gutendex.com/books/" + id;
            System.out.println("🔗 Consultando Gutendex: " + url);
            RestTemplate restTemplate = new RestTemplate();
            String jsonResponse = restTemplate.getForObject(url, String.class);

            JsonNode root = new ObjectMapper().readTree(jsonResponse);

            Libro libro = new Libro();

            if (root.has("summaries") && root.path("summaries").isArray() && root.path("summaries").size() > 0) {
                libro.setDescripcion(root.path("summaries").get(0).asText());
            } else {
                libro.setDescripcion(null);
            }

            List<String> categorias = new ArrayList<>();
            for (JsonNode subjectNode : root.path("subjects")) {
                categorias.add(subjectNode.asText());
            }
            libro.setCategorias(categorias);

            return libro;

        } catch (Exception e) {
            System.err.println("⚠️ Error obteniendo libro desde Gutendex: " + e.getMessage());
        }
        return null;
    }



    private String obtenerDescripcionDesdeGutendex(String titulo) {
        try {
            String url = "https://gutendex.com/books/?search=" + URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            JsonNode results = root.path("results");

            if (results.isArray() && results.size() > 0) {
                JsonNode primerLibro = results.get(0);
                JsonNode summaries = primerLibro.path("summaries");
                if (summaries.isArray() && summaries.size() > 0) {
                    return summaries.get(0).asText();
                }
            }
            return null;
        } catch (Exception e) {
            System.out.println("⚠️ Error al obtener descripción desde Gutendex: " + e.getMessage());
            return null;
        }
    }


    private String traducirADescripcionEspanol(String textoOriginal, String idiomaOrigen) {
        if (textoOriginal == null || textoOriginal.isBlank()) return textoOriginal;

            idiomaOrigen = detectarIdioma(textoOriginal);


        String idiomaNormalizado = idiomaOrigen.toLowerCase().trim();

        if (idiomaNormalizado.equals("es") || idiomaNormalizado.equals("spa")) {
            System.out.println("✅ Texto ya en español, no se traduce");
            return textoOriginal;
        }

        try {
            return traducirConLibreTranslate(textoOriginal, idiomaNormalizado, "es");

        } catch (Exception e) {
            System.out.println("⚠️ Error al traducir: " + e.getMessage());
            return textoOriginal;
        }
    }

    private String traducirConLibreTranslate(String texto, String idiomaOrigen, String idiomaDestino)
            throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> payload = new HashMap<>();
        payload.put("q", texto);
        payload.put("source", idiomaOrigen);
        payload.put("target", idiomaDestino);
        payload.put("format", "text");

        String jsonPayload = mapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5000/translate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Error HTTP " + response.statusCode() + ": " + response.body());
        }

        JsonNode root = mapper.readTree(response.body());
        String traduccion = root.path("translatedText").asText();

        if (traduccion == null || traduccion.isBlank()) {
            throw new Exception("Traducción vacía recibida");
        }

        System.out.println("✅ Traducido: " + texto.substring(0, Math.min(50, texto.length()))
                + "... → " + traduccion.substring(0, Math.min(50, traduccion.length())) + "...");

        return traduccion;
    }

    private static final LanguageDetector detector = LanguageDetectorBuilder
            .fromAllLanguages()
            .build();

    private String detectarIdioma(String texto) {
        try {
            Language idioma = detector.detectLanguageOf(texto);
            String codigo = idioma.getIsoCode639_1().toString().toLowerCase();
            System.out.println("🔍 Idioma detectado (local): " + codigo);
            return codigo;
        } catch (Exception e) {
            System.out.println("⚠️ Error al detectar idioma: " + e.getMessage());
            return "en";
        }
    }

    public List<Libro> buscarLibrosMasPopulares(){
        return libroRepository.buscarLibrosPopulares();


    }



    private String normalizarTexto(String valor) {
        return valor != null ? valor.trim().toLowerCase() : "";
    }





    public Page<Libro> buscarLibrosEnBd(String titulo, String autor, String idioma, String idiomaQ, int page) {
        String tituloQ = titulo != null ? titulo.trim().toLowerCase() : "";
        String autorQ = autor != null ? autor.trim().toLowerCase() : "";
        int pageSize = 32;

        System.out.println(idiomaQ);
        System.out.println(StringUtils.hasText(idioma));
        System.out.println(StringUtils.hasText(autor));
        System.out.println(StringUtils.hasText(titulo));

        // 🔹 Buscar por idioma solamente
        if (StringUtils.hasText(idioma) && !StringUtils.hasText(autor) && !StringUtils.hasText(titulo)) {
            System.out.println("buscar por idioma");
            System.out.println("idioma " + idiomaQ);
            System.out.println("page " + page);

            Optional<PaginasGuardadas> paginaGuardadaBuscar =
                    paginasGuardadasRepository.findByIdiomaAndNumeroPaginaConLibrosYAutores(idiomaQ, page);

            if (paginaGuardadaBuscar.isPresent()) {
                System.out.println("✅ Página encontrada en BD (" + idiomaQ + " pág. " + page + ")");
                List<Libro> librosGuardados = paginaGuardadaBuscar.get()
                        .getLibroPaginas()
                        .stream()
                        .map(LibroPagina::getLibro)
                        .toList();

                return new PageImpl<>(
                        librosGuardados,
                        PageRequest.of(page - 1, pageSize),
                        paginaGuardadaBuscar.get().getTotalRegistros()
                );
            }
        }

        // 🔹 Buscar por idioma + autor
        else if (StringUtils.hasText(idioma) && StringUtils.hasText(autor) && !StringUtils.hasText(titulo)) {
            Optional<List<Libro>> librosPorAutor = libroRepository.buscarPorIdiomaYAutor(idiomaQ, autorQ);

            if (librosPorAutor.isPresent() && !librosPorAutor.get().isEmpty()) {
                return construirPagina(librosPorAutor, page, pageSize);
            }
        }

        // 🔹 Buscar por idioma + título
        else if (StringUtils.hasText(idioma) && !StringUtils.hasText(autor) && StringUtils.hasText(titulo)) {
            Optional<List<Libro>> librosPorTitulo =
                    libroRepository.buscarPorIdiomaYTituloOAutor(idiomaQ, tituloQ, tituloQ);

            if (librosPorTitulo.isPresent() && !librosPorTitulo.get().isEmpty()) {
                return construirPagina(librosPorTitulo, page, pageSize);
            }
        }

        // 🔹 Buscar por idioma + autor + título
        else if (StringUtils.hasText(idioma) && StringUtils.hasText(autor) && StringUtils.hasText(titulo)) {
            Optional<List<Libro>> librosPorAmbos =
                    libroRepository.buscarPorIdiomaAutorYTitulo(idiomaQ, tituloQ, autorQ);

            if (librosPorAmbos.isPresent() && !librosPorAmbos.get().isEmpty()) {
                return construirPagina(librosPorAmbos, page, pageSize);
            }
        }

        return null;
    }




    private Page<Libro> construirPagina(Optional<List<Libro>> librosOpt, int page, int pageSize) {
        if (librosOpt.isEmpty() || librosOpt.get().isEmpty()) {
            return Page.empty();
        }

        List<Libro> libros = librosOpt.get();
        int total = libros.size();

        int fromIndex = Math.min((page - 1) * pageSize, total);
        int toIndex = Math.min(fromIndex + pageSize, total);

        List<Libro> pageContent = libros.subList(fromIndex, toIndex);
        return new PageImpl<>(pageContent, PageRequest.of(page - 1, pageSize), total);
    }



    //Es quien realmente realiza la búsqueda, accede
    // a la base de datos, Gutendex, etc.

    @Override
    public Page<Libro> buscarLibros(String titulo, String autor, String idioma, int page) throws IOException {
        BuscarInvoker invoker = new BuscarInvoker();
        BuscarCommand command = invoker.build(titulo, autor, idioma, page, this);

        try {
            return command.ejecutar();
        } catch (Exception e) {
            throw new IOException("Error ejecutando command de búsqueda", e);
        }
    }


}

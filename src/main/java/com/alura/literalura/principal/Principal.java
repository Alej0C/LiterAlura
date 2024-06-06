package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private final LibroRepository repositoryLibro;
    private final AutorRepository repositoryAutor;

    public Principal(LibroRepository repositoryLibro, AutorRepository repositoryAutor) {
        this.repositoryLibro = repositoryLibro;
        this.repositoryAutor = repositoryAutor;
    }

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            String menu = """
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idiomas
                    0 - Salir
                    """;
            System.out.println(menu);
            while (!teclado.hasNextInt()) {
                System.out.println("Formato inválido, ingrese un número que esté disponible en el menú");
                teclado.nextLine();
            }
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1 -> buscarLibroTitulo();
                case 2 -> buscarLibroRegistro();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivos();
                case 5 -> listarLibrosIdiomas();
                case 0 -> System.out.println("Cerrando la aplicación...");
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private DatosBusqueda getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        String nombreLibro = teclado.nextLine();
        String json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));
        return conversor.obtenerDatos(json, DatosBusqueda.class);
    }

    private void buscarLibroTitulo() {
        DatosBusqueda datosBusqueda = getDatosLibro();
        if (datosBusqueda == null || datosBusqueda.resultado().isEmpty()) {
            System.out.println("Libro no encontrado");
            return;
        }

        DatosLibros primerLibro = datosBusqueda.resultado().getFirst();
        Libro libro = new Libro(primerLibro);
        System.out.println("----- Libro -----");
        System.out.println(libro);
        System.out.println("-----------------");

        Optional<Libro> libroExistenteOptional = repositoryLibro.findByTitulo(libro.getTitulo());
        if (libroExistenteOptional.isPresent()) {
            System.out.println("\nEl libro ya está registrado\n");
            return;
        }

        if (primerLibro.autor().isEmpty()) {
            System.out.println("Sin autor");
            return;
        }

        DatosAutor datosAutor = primerLibro.autor().getFirst();
        Autor autor = new Autor(datosAutor);
        Optional<Autor> autorOptional = repositoryAutor.findByNombre(autor.getNombre());

        Autor autorExistente = autorOptional.orElseGet(() -> repositoryAutor.save(autor));
        libro.setAutor(autorExistente);
        repositoryLibro.save(libro);

        System.out.printf("""
                ---------- Libro ----------
                Título: %s
                Autor: %s
                Idioma: %s
                Número de Descargas: %d
                ---------------------------
                """, libro.getTitulo(), autor.getNombre(), libro.getLenguaje(), libro.getNumeroDescargas());
    }

    private void buscarLibroRegistro() {
        List<Libro> libros = repositoryLibro.findAll();

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros registrados.");
            return;
        }

        System.out.println("----- Libros Registrados -----");
        libros.forEach(System.out::println);
        System.out.println("-------------------------------");
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = repositoryAutor.findAll();

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores registrados.");
            return;
        }

        System.out.println("----- Autores Registrados -----");
        autores.forEach(System.out::println);
        System.out.println("--------------------------------");
    }

    private void listarAutoresVivos() {
        System.out.println("Introduce el año para listar los autores vivos:");
        while (!teclado.hasNextInt()) {
            System.out.println("Formato inválido, ingrese un número válido para el año");
            teclado.nextLine();
        }
        int anio = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autores = repositoryAutor.findAutoresVivosEnAnio(anio);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio);
        } else {
            System.out.println("----- Autores Vivos en el Año " + anio + " -----");
            autores.forEach(System.out::println);
            System.out.println("---------------------------------------------");
        }
    }

    private void listarLibrosIdiomas() {
        System.out.println("Selecciona el lenguaje/idioma que deseas buscar: ");
        while (true) {
            String opciones = """
                    1. en - Inglés
                    2. es - Español
                    3. fr - Francés
                    4. pt - Portugués
                    0. Volver a las opciones anteriores
                    """;
            System.out.println(opciones);
            while (!teclado.hasNextInt()) {
                System.out.println("Formato inválido, ingrese un número que esté disponible en el menú");
                teclado.nextLine();
            }
            int opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1 -> mostrarLibrosPorIdioma(Idioma.en);
                case 2 -> mostrarLibrosPorIdioma(Idioma.es);
                case 3 -> mostrarLibrosPorIdioma(Idioma.fr);
                case 4 -> mostrarLibrosPorIdioma(Idioma.pt);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private void mostrarLibrosPorIdioma(Idioma idioma) {
        List<Libro> librosPorIdioma = repositoryLibro.findByLenguaje(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en " + idioma.getIdiomaEspanol());
        } else {
            System.out.printf("----- Libros en %s ----- %n", idioma.getIdiomaEspanol());
            librosPorIdioma.forEach(System.out::println);
            System.out.println("-----------------------------");
        }
    }
}

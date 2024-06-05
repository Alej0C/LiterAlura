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
    private LibroRepository repositoryLibro;
    private AutorRepository repositoryAutor;
    private List<Autor> autores;
    private List<Libro> libros;

    public Principal(LibroRepository repositoryLibro, AutorRepository repositoryAutor) {
        this.repositoryLibro = repositoryLibro;
        this.repositoryAutor = repositoryAutor;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - listar libros registrados
                    3 - listar autores registrados
                    4 - listar autores vivos en un determinado año
                    5 - listar libros por idiomas
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroTitulo();
                    break;
                case 2:
                    buscarLibroRegistro();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    // listarAutoresVivos();
                    break;
                case 5:
                    // listarLibrosIdiomas();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
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
                """, libro.getTitulo(), autor.getNombre(), libro.getLenguaje(), libro.getNumero_descargas());
    }

    private void buscarLibroRegistro() {
        libros = repositoryLibro.findAll();

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros registrados.");
            return;
        }

        System.out.println("----- Libros Registrados -----");
        for (Libro libro : libros) {
            System.out.println(libro);
        }
        System.out.println("-------------------------------");
    }

    private void listarAutoresRegistrados() {
        autores = repositoryAutor.findAll();

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores registrados.");
            return;
        }

        System.out.println("----- Autores Registrados -----");
        autores.forEach(System.out::println);
        System.out.println("--------------------------------");
    }
}

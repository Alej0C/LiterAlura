package com.alura.literalura.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String nombre;
    private Integer fechaNacimiento;
    private Integer fechaDeceso;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor() {
    }

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = datosAutor.fechaNacimiento();
        this.fechaDeceso = datosAutor.fechaFallecimiento();
    }

    @Override
    public String toString() {
        StringBuilder librosStr = new StringBuilder();
        librosStr.append("Libros: ");
        for (int i = 0; i < libros.size(); i++) {
            librosStr.append(libros.get(i).getTitulo());
            if (i < libros.size() - 1) {
                librosStr.append(", ");
            }
        }
        return String.format("---------- Autor ----------%nNombre: %s%n%s%nAño de Nacimiento: %d%nAño de Deceso: %d%n---------------------------%n",
                nombre, librosStr, fechaNacimiento, fechaDeceso == null ? "Desconocido" : fechaDeceso);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaDeceso() {
        return fechaDeceso;
    }

    public void setFechaDeceso(Integer fechaDeceso) {
        this.fechaDeceso = fechaDeceso;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}

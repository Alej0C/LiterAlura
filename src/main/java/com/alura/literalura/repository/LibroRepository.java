package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    // Método para buscar libros por título exacto
    Optional<Libro> findByTitulo(String titulo);

    // Método para buscar libros por título parcial (contiene)
    @Query("SELECT l FROM Libro l WHERE l.titulo LIKE %:titulo%")
    List<Libro> findByTituloContaining(@Param("titulo") String titulo);
}

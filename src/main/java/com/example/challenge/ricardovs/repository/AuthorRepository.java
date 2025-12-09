package com.example.challenge.ricardovs.repository;

import com.example.challenge.ricardovs.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Derived query alternativo usando JPQL para autores que estaban vivos en un año dado.
    @Query("SELECT a FROM Author a WHERE a.birthYear <= :year AND (a.deathYear IS NULL OR a.deathYear >= :year)")
    List<Author> findAuthorsAliveInYear(int year);

    Author findByName(String name);
}

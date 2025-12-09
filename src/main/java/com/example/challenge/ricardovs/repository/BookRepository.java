package com.example.challenge.ricardovs.repository;

import com.example.challenge.ricardovs.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByLanguage(String language);
}

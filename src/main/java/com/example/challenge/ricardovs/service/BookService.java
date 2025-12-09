package com.example.challenge.ricardovs.service;

import com.example.challenge.ricardovs.client.GutendexClient;
import com.example.challenge.ricardovs.dto.GutendexBookDto;
import com.example.challenge.ricardovs.dto.GutendexAuthorDto;
import com.example.challenge.ricardovs.model.Author;
import com.example.challenge.ricardovs.model.Book;
import com.example.challenge.ricardovs.repository.AuthorRepository;
import com.example.challenge.ricardovs.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final GutendexClient gutendexClient;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(GutendexClient gutendexClient, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.gutendexClient = gutendexClient;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    /**
     * Busca el primer resultado de Gutendex por título y lo guarda (autor + libro).
     */
    @Transactional
    public void searchAndSaveByTitle(String title) throws IOException, InterruptedException {
        List<GutendexBookDto> results = gutendexClient.searchByTitle(title);
        if (results.isEmpty()) {
            System.out.println("No se encontró ningún libro con ese título.");
            return;
        }

        GutendexBookDto dto = results.get(0); // tomamos el primer resultado
        Integer gutId = dto.getId();
        String bookTitle = dto.getTitle();
        Integer downloads = dto.getDownloadCount();
        String language = "unknown";
        if (dto.getLanguages() != null && !dto.getLanguages().isEmpty()) {
            language = dto.getLanguages().get(0);
        }

        // autor: primer autor de la lista
        Author authorEntity = null;
        if (dto.getAuthors() != null && !dto.getAuthors().isEmpty()) {
            GutendexAuthorDto a = dto.getAuthors().get(0);
            String name = a.getName();
            Integer birth = a.getBirth_year();
            Integer death = a.getDeath_year();

            Author existing = authorRepository.findByName(name);
            if (existing != null) {
                authorEntity = existing;
            } else {
                authorEntity = new Author(name, birth, death);
                authorRepository.save(authorEntity);
            }
        } else {
            // autor desconocido
            authorEntity = new Author("Desconocido", null, null);
            authorRepository.save(authorEntity);
        }

        Book book = new Book(gutId, bookTitle, language, downloads, authorEntity);
        bookRepository.save(book);
        System.out.println("Libro guardado: " + book);
    }

    public List<Book> listAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> listByLanguage(String lang) {
        return bookRepository.findByLanguage(lang);
    }

    public List<Author> listAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Author> authorsAliveInYear(int year) {
        return authorRepository.findAuthorsAliveInYear(year);
    }

    public void printCountByLanguage() {
        List<Book> all = bookRepository.findAll();
        Map<String, Long> map = all.stream()
                .collect(Collectors.groupingBy(b -> b.getLanguage() == null ? "unknown" : b.getLanguage(), Collectors.counting()));

        System.out.println("Cantidad de libros por idioma:");
        map.forEach((k, v) -> System.out.println(k + ": " + v));
    }
}

package com.example.challenge.ricardovs.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer gutendexId;
    private String title;
    private String language;
    private Integer downloadCount;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id")
    private Author author;

    // constructors
    public Book() {}
    public Book(Integer gutendexId, String title, String language, Integer downloadCount, Author author) {
        this.gutendexId = gutendexId;
        this.title = title;
        this.language = language;
        this.downloadCount = downloadCount;
        this.author = author;
    }

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getGutendexId() { return gutendexId; }
    public void setGutendexId(Integer gutendexId) { this.gutendexId = gutendexId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public Integer getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    @Override
    public String toString() {
        return "Book{id=" + id +
                ", gutendexId=" + gutendexId +
                ", title='" + title + '\'' +
                ", language='" + language + '\'' +
                ", downloadCount=" + downloadCount +
                ", author=" + (author != null ? author.getName() : "N/A") +
                '}';
    }
}

package com.example.literalura;

import com.example.literalura.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(LiterAluraApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- MENÚ LITERALURA ---");
            System.out.println("1. Buscar libro por título y guardar");
            System.out.println("2. Listar todos los libros guardados");
            System.out.println("3. Listar libros por idioma");
            System.out.println("4. Listar autores");
            System.out.println("5. Listar autores vivos en un año");
            System.out.println("6. Estadística: cantidad de libros por idioma");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            String option = scanner.nextLine().trim();
            try {
                switch (option) {
                    case "1":
                        System.out.print("Ingrese título a buscar: ");
                        String title = scanner.nextLine().trim();
                        bookService.searchAndSaveByTitle(title);
                        break;
                    case "2":
                        bookService.listAllBooks().forEach(System.out::println);
                        break;
                    case "3":
                        System.out.print("Ingrese idioma (por ejemplo 'en'): ");
                        String lang = scanner.nextLine().trim();
                        bookService.listByLanguage(lang).forEach(System.out::println);
                        break;
                    case "4":
                        bookService.listAllAuthors().forEach(System.out::println);
                        break;
                    case "5":
                        System.out.print("Ingrese año (por ejemplo 1900): ");
                        int year = Integer.parseInt(scanner.nextLine().trim());
                        bookService.authorsAliveInYear(year).forEach(System.out::println);
                        break;
                    case "6":
                        bookService.printCountByLanguage();
                        break;
                    case "0":
                        System.out.println("Saliendo. ¡Hasta luego!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
}

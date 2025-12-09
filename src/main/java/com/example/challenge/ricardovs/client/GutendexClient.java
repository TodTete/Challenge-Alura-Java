package com.example.challenge.ricardovs.client;

import com.example.challenge.ricardovs.dto.GutendexBookDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class GutendexClient {

    private static final String BASE = "https://gutendex.com/books";
    private final HttpClient client;
    private final ObjectMapper mapper;

    public GutendexClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .version(HttpClient.Version.HTTP_2)
                .build();
        this.mapper = new ObjectMapper();
    }

    /**
     * Busca libros por título en Gutendex y devuelve la lista de resultados como GutendexBookDto.
     */
    public List<GutendexBookDto> searchByTitle(String title) throws IOException, InterruptedException {
        String url = BASE + "?search=" + URI.create(title).toString().replace(" ", "%20");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(20))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Respuesta inválida de Gutendex: " + response.statusCode());
        }

        JsonNode root = mapper.readTree(response.body());
        JsonNode results = root.path("results");
        List<GutendexBookDto> list = new ArrayList<>();
        if (results.isArray()) {
            for (JsonNode node : results) {
                GutendexBookDto dto = mapper.treeToValue(node, GutendexBookDto.class);
                list.add(dto);
            }
        }
        return list;
    }
}

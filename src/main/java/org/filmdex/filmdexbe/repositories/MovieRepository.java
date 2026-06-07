package org.filmdex.filmdexbe.repositories;

import org.filmdex.filmdexbe.configuration.ConfigurationResponse;
import org.filmdex.filmdexbe.models.Movie;
import org.springframework.beans.factory.annotation.Value; // <-- Nueva importación
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;
import java.util.List;

@Repository
public class MovieRepository {

    private final RestClient restClient = RestClient.create("https://api.themoviedb.org/3");

    @Value("${tmdb.api.token:miToken}") // <-- Lee de application.properties, usa "miToken" por defecto
    private String token;

    public record TmdbResponse(List<Movie> results) {}

    public ConfigurationResponse getConfiguration() {
        return restClient.get()
                .uri("/configuration")
                .header("Authorization", "Bearer " + token) // <-- Usa el token configurado
                .retrieve()
                .body(ConfigurationResponse.class);
    }

    public List<Movie> getAllMovies() {
        List<Movie> allMovies = new java.util.ArrayList<>();
        for (int page = 1; page <= 3; page++) {
            try {
                TmdbResponse response = restClient.get()
                        .uri("/movie/popular?language=es-ES&page=" + page)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .body(TmdbResponse.class);
                if (response != null && response.results() != null) {
                    allMovies.addAll(response.results());
                }
            } catch (Exception e) {
                // Ignorar error de página para robustez
            }
        }
        if (allMovies.size() > 50) {
            return allMovies.subList(0, 50);
        }
        return allMovies;
    }

    public Movie getMovieById(Long id) {
        return restClient.get()
                .uri("/movie/{id}?language=es-ES&append_to_response=credits", id)
                .header("Authorization", "Bearer " + token) // <-- Usa el token configurado
                .retrieve()
                .body(Movie.class);
    }
}
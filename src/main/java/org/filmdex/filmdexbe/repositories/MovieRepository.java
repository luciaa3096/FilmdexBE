package org.filmdex.filmdexbe.repositories;

import org.filmdex.filmdexbe.configuration.ConfigurationResponse;
import org.filmdex.filmdexbe.models.Movie;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.List;

@Repository
public class MovieRepository {

    private final RestClient restClient = RestClient.create("https://api.themoviedb.org/3");
    private final String TOKEN = "miToken";

    // Truco sencillo: TMDB devuelve las listas dentro de una propiedad "results".
    // Usamos esto para atrapar esa lista fácilmente.
    public record TmdbResponse(List<Movie> results) {}

    public ConfigurationResponse getConfiguration() {
        return restClient.get()
                .uri("/configuration")
                .header("Authorization", "Bearer " + TOKEN)
                .retrieve()
                .body(ConfigurationResponse.class);
    }

    public List<Movie> getAllMovies() {
        return restClient.get()
                .uri("/movie/popular?language=es-ES")
                .header("Authorization", "Bearer " + TOKEN)
                .retrieve()
                .body(TmdbResponse.class)
                .results(); // Extraemos solo la lista
    }

    public Movie getMovieById(Long id) {
        return restClient.get()
                .uri("/movie/{id}?language=es-ES", id)
                .header("Authorization", "Bearer " + TOKEN)
                .retrieve()
                .body(Movie.class);
    }

}
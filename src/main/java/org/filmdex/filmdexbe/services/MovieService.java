package org.filmdex.filmdexbe.services;

import org.filmdex.filmdexbe.configuration.ConfigurationResponse;
import org.filmdex.filmdexbe.models.Movie;
import org.filmdex.filmdexbe.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public ConfigurationResponse getConfiguration() {
        return movieRepository.getConfiguration();
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = movieRepository.getAllMovies();
        if (movies == null) {
            return Collections.emptyList();
        }
        return movies.parallelStream()
                .map(m -> {
                    try {
                        Movie detailed = movieRepository.getMovieById(m.getId());
                        return detailed != null ? detailed : m;
                    } catch (Exception e) {
                        return m;
                    }
                })
                .collect(Collectors.toList());
    }

    public Movie getMovieById(Long id) {
        try {
            return movieRepository.getMovieById(id);
        } catch (Exception e) {
            return null;
        }
    }

}
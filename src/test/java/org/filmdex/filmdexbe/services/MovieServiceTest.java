package org.filmdex.filmdexbe.services;

import org.filmdex.filmdexbe.configuration.ConfigurationResponse;
import org.filmdex.filmdexbe.configuration.ImagesConfig;
import org.filmdex.filmdexbe.models.Movie;
import org.filmdex.filmdexbe.repositories.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie sampleMovie;
    private Movie detailedMovie;

    @BeforeEach
    void setUp() {
        sampleMovie = new Movie();
        sampleMovie.setId(1L);
        sampleMovie.setTitle("Interstellar");
        sampleMovie.setYear(2014);

        detailedMovie = new Movie();
        detailedMovie.setId(1L);
        detailedMovie.setTitle("Interstellar Detailed");
        detailedMovie.setYear(2014);
        detailedMovie.setRuntime(169);
    }

    @Test
    void testGetAllMovies_Success() {
        when(movieRepository.getAllMovies()).thenReturn(List.of(sampleMovie));
        when(movieRepository.getMovieById(1L)).thenReturn(detailedMovie);

        List<Movie> result = movieService.getAllMovies();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Interstellar Detailed", result.get(0).getTitle());
        verify(movieRepository, times(1)).getAllMovies();
        verify(movieRepository, times(1)).getMovieById(1L);
    }

    @Test
    void testGetAllMovies_NullResponse() {
        when(movieRepository.getAllMovies()).thenReturn(null);

        List<Movie> result = movieService.getAllMovies();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(movieRepository, times(1)).getAllMovies();
        verify(movieRepository, never()).getMovieById(anyLong());
    }

    @Test
    void testGetAllMovies_DetailFetchErrorFallback() {
        when(movieRepository.getAllMovies()).thenReturn(List.of(sampleMovie));
        when(movieRepository.getMovieById(1L)).thenThrow(new RuntimeException("TMDB error"));

        List<Movie> result = movieService.getAllMovies();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Interstellar", result.get(0).getTitle()); // Falls back to sample movie
        verify(movieRepository, times(1)).getAllMovies();
        verify(movieRepository, times(1)).getMovieById(1L);
    }

    @Test
    void testGetMovieById_Success() {
        when(movieRepository.getMovieById(1L)).thenReturn(detailedMovie);

        Movie result = movieService.getMovieById(1L);

        assertNotNull(result);
        assertEquals("Interstellar Detailed", result.getTitle());
        verify(movieRepository, times(1)).getMovieById(1L);
    }

    @Test
    void testGetMovieById_ErrorReturnsNull() {
        when(movieRepository.getMovieById(2L)).thenThrow(new RuntimeException("API error"));

        Movie result = movieService.getMovieById(2L);

        assertNull(result);
        verify(movieRepository, times(1)).getMovieById(2L);
    }

    @Test
    void testGetConfiguration() {
        ConfigurationResponse expectedConfig = new ConfigurationResponse(new ImagesConfig("http://base", "https://base", List.of(), List.of(), List.of(), List.of(), List.of()));
        when(movieRepository.getConfiguration()).thenReturn(expectedConfig);

        ConfigurationResponse result = movieService.getConfiguration();

        assertNotNull(result);
        verify(movieRepository, times(1)).getConfiguration();
    }
}
package org.filmdex.filmdexbe.services;

import org.filmdex.filmdexbe.models.Movie;
import org.filmdex.filmdexbe.repositories.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie sampleMovie;

    @BeforeEach
    void setUp() {
        sampleMovie = new Movie();
        sampleMovie.setId(1L);
        sampleMovie.setTitle("Interstellar");
        sampleMovie.setYear(2014);
    }

    @Test
    void testGetAllMovies() {
        when(movieRepository.findAll()).thenReturn(List.of(sampleMovie));

        List<Movie> result = movieService.getAllMovies();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Interstellar", result.get(0).getTitle());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void testGetMovieById_Found() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(sampleMovie));

        Movie result = movieService.getMovieById(1L);

        assertNotNull(result);
        assertEquals("Interstellar", result.getTitle());
    }

    @Test
    void testGetMovieById_NotFound() {
        when(movieRepository.findById(2L)).thenReturn(Optional.empty());

        Movie result = movieService.getMovieById(2L);

        assertNull(result); // Refleja tu código: .orElse(null)
    }

    @Test
    void testSaveMovie() {
        when(movieRepository.save(sampleMovie)).thenReturn(sampleMovie);

        Movie result = movieService.saveMovie(sampleMovie);

        assertNotNull(result);
        assertEquals("Interstellar", result.getTitle());
        verify(movieRepository, times(1)).save(sampleMovie);
    }

    @Test
    void testDeleteMovie() {
        doNothing().when(movieRepository).deleteById(1L);

        movieService.deleteMovie(1L);

        verify(movieRepository, times(1)).deleteById(1L);
    }
}
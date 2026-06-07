package org.filmdex.filmdexbe.services;

import org.filmdex.filmdexbe.models.Movie;
import org.filmdex.filmdexbe.models.Review;
import org.filmdex.filmdexbe.repositories.MovieDbRepository;
import org.filmdex.filmdexbe.repositories.MovieRepository;
import org.filmdex.filmdexbe.repositories.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MovieDbRepository movieDbRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review sampleReview;
    private Movie sampleMovie;

    @BeforeEach
    void setUp() {
        sampleMovie = new Movie();
        sampleMovie.setId(10L);
        sampleMovie.setTitle("Inception");

        sampleReview = new Review();
        sampleReview.setId(1L);
        sampleReview.setRating(9);
        sampleReview.setComment("Excelente película, muy recomendada.");
        sampleReview.setMovie(sampleMovie);
    }

    @Test
    void testGetAllReviews() {
        when(reviewRepository.findAll()).thenReturn(List.of(sampleReview));

        List<Review> result = reviewService.getAllReviews();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(9, result.get(0).getRating());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void testSaveReview_MovieAlreadyExists() {
        // Mock that the movie already exists in the local database
        when(movieDbRepository.existsById(10L)).thenReturn(true);
        when(reviewRepository.save(sampleReview)).thenReturn(sampleReview);

        Review result = reviewService.saveReview(sampleReview);

        assertNotNull(result);
        assertEquals("Excelente película, muy recomendada.", result.getComment());
        verify(movieDbRepository, times(1)).existsById(10L);
        verify(movieRepository, never()).getMovieById(anyLong());
        verify(movieDbRepository, never()).save(any(Movie.class));
        verify(reviewRepository, times(1)).save(sampleReview);
    }

    @Test
    void testSaveReview_MovieDoesNotExist_FetchFromTmdbSuccess() {
        // Mock that the movie does not exist locally
        when(movieDbRepository.existsById(10L)).thenReturn(false);
        // Mock fetch from TMDB
        Movie tmdbMovie = new Movie();
        tmdbMovie.setId(10L);
        tmdbMovie.setTitle("Inception TMDB");
        when(movieRepository.getMovieById(10L)).thenReturn(tmdbMovie);
        when(movieDbRepository.save(any(Movie.class))).thenReturn(tmdbMovie);
        when(reviewRepository.save(sampleReview)).thenReturn(sampleReview);

        Review result = reviewService.saveReview(sampleReview);

        assertNotNull(result);
        verify(movieDbRepository, times(1)).existsById(10L);
        verify(movieRepository, times(1)).getMovieById(10L);
        verify(movieDbRepository, times(1)).save(any(Movie.class));
        verify(reviewRepository, times(1)).save(sampleReview);
    }

    @Test
    void testSaveReview_MovieDoesNotExist_TmdbFailsFallback() {
        // Mock that the movie does not exist locally
        when(movieDbRepository.existsById(10L)).thenReturn(false);
        // Mock fetch from TMDB throwing exception
        when(movieRepository.getMovieById(10L)).thenThrow(new RuntimeException("API error"));
        when(movieDbRepository.save(any(Movie.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(reviewRepository.save(sampleReview)).thenReturn(sampleReview);

        Review result = reviewService.saveReview(sampleReview);

        assertNotNull(result);
        verify(movieDbRepository, times(1)).existsById(10L);
        verify(movieRepository, times(1)).getMovieById(10L);
        verify(movieDbRepository, times(1)).save(argThat(m -> m.getTitle().equals("Película 10")));
        verify(reviewRepository, times(1)).save(sampleReview);
    }

    @Test
    void testGetReviewsByMovie() {
        when(reviewRepository.findByMovieId(10L)).thenReturn(List.of(sampleReview));

        List<Review> result = reviewService.getReviewsByMovie(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getMovie().getId());
        verify(reviewRepository, times(1)).findByMovieId(10L);
    }
}
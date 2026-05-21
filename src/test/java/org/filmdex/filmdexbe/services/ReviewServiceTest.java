package org.filmdex.filmdexbe.services;

import org.filmdex.filmdexbe.models.Movie;
import org.filmdex.filmdexbe.models.Review;
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
    void testSaveReview() {
        when(reviewRepository.save(sampleReview)).thenReturn(sampleReview);

        Review result = reviewService.saveReview(sampleReview);

        assertNotNull(result);
        assertEquals("Excelente película, muy recomendada.", result.getComment());
        verify(reviewRepository, times(1)).save(sampleReview);
    }

    @Test
    void testGetReviewsByMovie() {
        // Configuramos el mock para responder cuando busquemos por el ID de la película (10L)
        when(reviewRepository.findByMovieId(10L)).thenReturn(List.of(sampleReview));

        List<Review> result = reviewService.getReviewsByMovie(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getMovie().getId());
        verify(reviewRepository, times(1)).findByMovieId(10L);
    }
}
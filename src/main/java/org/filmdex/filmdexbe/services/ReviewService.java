package org.filmdex.filmdexbe.services;

import org.filmdex.filmdexbe.models.Review;
import org.filmdex.filmdexbe.models.Movie;
import org.filmdex.filmdexbe.repositories.ReviewRepository;
import org.filmdex.filmdexbe.repositories.MovieDbRepository;
import org.filmdex.filmdexbe.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieDbRepository movieDbRepository;

    @Autowired
    private MovieRepository movieRepository;

    public Review saveReview(Review review) {
        if (review.getMovie() != null && review.getMovie().getId() != null) {
            Long movieId = review.getMovie().getId();
            if (!movieDbRepository.existsById(movieId)) {
                try {
                    Movie tmdbMovie = movieRepository.getMovieById(movieId);
                    if (tmdbMovie != null) {
                        tmdbMovie.setDirector(null);
                        tmdbMovie.setCast(null);
                        tmdbMovie.setReviews(null);
                        movieDbRepository.save(tmdbMovie);
                    }
                } catch (Exception e) {
                    Movie basicMovie = new Movie();
                    basicMovie.setId(movieId);
                    basicMovie.setTitle("Película " + movieId);
                    movieDbRepository.save(basicMovie);
                }
            }
        }
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByMovie(Long movieId) {
        return reviewRepository.findByMovieId(movieId);
    }
}
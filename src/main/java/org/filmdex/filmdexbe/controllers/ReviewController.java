package org.filmdex.filmdexbe.controllers;

import org.filmdex.filmdexbe.models.Review;
import org.filmdex.filmdexbe.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public List<Review> getAll() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/movie/{movieId}")
    public List<Review> getByMovie(@PathVariable Long movieId) {
        return reviewService.getReviewsByMovie(movieId);
    }

    @PostMapping
    public Review create(@RequestBody Review review) {
        return reviewService.saveReview(review);
    }
}
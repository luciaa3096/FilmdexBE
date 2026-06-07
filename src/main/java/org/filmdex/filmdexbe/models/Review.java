package org.filmdex.filmdexbe.models;
import jakarta.persistence.*;

@Entity
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "movie_id"})
})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_seq")
    @SequenceGenerator(name = "reviews_seq", sequenceName = "reviews_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("review")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("reviews")
    private Movie movie;

    @Column(nullable = false)
    private Integer rating;

    @Lob
    @Column(name = "\"comment\"")
    private String comment;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
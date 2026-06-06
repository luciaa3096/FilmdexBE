package org.filmdex.filmdexbe.models;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "movie_id"})
})
@Data
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
}
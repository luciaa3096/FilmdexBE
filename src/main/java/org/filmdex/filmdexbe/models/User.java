package org.filmdex.filmdexbe.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users") // 'user' suele ser una palabra reservada en SQL, es mejor usar 'users'
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    private Long id;

    private String name;
    @Column(unique = true)
    private String username;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("user")
    private List<Review> review;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_watched_movies", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "movie_id")
    private List<Long> watched;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_favourite_movies", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "movie_id")
    private List<Long> favourites;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Review> getReview() {
        return review;
    }

    public void setReview(List<Review> review) {
        this.review = review;
    }

    public List<Long> getWatched() {
        return watched;
    }

    public void setWatched(List<Long> watched) {
        this.watched = watched;
    }

    public List<Long> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<Long> favourites) {
        this.favourites = favourites;
    }
}
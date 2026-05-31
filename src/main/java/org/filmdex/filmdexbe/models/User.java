package org.filmdex.filmdexbe.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users") // 'user' suele ser una palabra reservada en SQL, es mejor usar 'users'
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(unique = true)
    private String username;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> review;

    @ElementCollection
    @CollectionTable(name = "user_watched_movies", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "movie_id")
    private List<Long> watched;


    @ElementCollection
    @CollectionTable(name = "user_favourite_movies", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "movie_id")
    private List<Long> favourites;


}
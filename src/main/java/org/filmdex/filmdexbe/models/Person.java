package org.filmdex.filmdexbe.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "persons_seq")
    @SequenceGenerator(name = "persons_seq", sequenceName = "persons_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "director")
    @JsonIgnore
    private List<Movie> directedMovies;

    @ManyToMany(mappedBy = "cast")
    @JsonIgnore
    private List<Movie> movies;

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

    public List<Movie> getDirectedMovies() {
        return directedMovies;
    }

    public void setDirectedMovies(List<Movie> directedMovies) {
        this.directedMovies = directedMovies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
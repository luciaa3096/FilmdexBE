package org.filmdex.filmdexbe.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "persons")
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "director")
    @JsonIgnore
    private List<Movie> directedMovies;

    @ManyToMany(mappedBy = "cast")
    @JsonIgnore
    private List<Movie> movies;
}
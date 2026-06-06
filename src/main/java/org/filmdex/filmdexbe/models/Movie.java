package org.filmdex.filmdexbe.models;

import com.fasterxml.jackson.annotation.JsonProperty; // <-- Nueva importación de Jackson
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "movies")
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movies_seq")
    @SequenceGenerator(name = "movies_seq", sequenceName = "movies_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String title;

    private Integer year;

    @Lob
    @JsonProperty("overview")
    private String synopsis;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Person director;

    @ManyToMany
    @JoinTable(
            name = "movie_cast",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private List<Person> cast;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("movie")
    private List<Review> reviews;

    // pasar año de TMDB de string a int
    @JsonProperty("release_date")
    public void setYearFromReleaseDate(String releaseDate) {
        if (releaseDate != null && releaseDate.length() >= 4) {
            try {
                this.year = Integer.parseInt(releaseDate.substring(0, 4));
            } catch (NumberFormatException e) {
                this.year = null;
            }
        }
    }
}
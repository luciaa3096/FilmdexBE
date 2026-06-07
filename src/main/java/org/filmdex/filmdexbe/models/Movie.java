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
    private Long id;

    @Column(nullable = false)
    private String title;

    private Integer year;

    @Lob
    @JsonProperty("synopsis")
    @com.fasterxml.jackson.annotation.JsonAlias("overview")
    private String synopsis;

    @JsonProperty("posterPath")
    @com.fasterxml.jackson.annotation.JsonAlias("poster_path")
    private String posterPath;

    @JsonProperty("voteAverage")
    @com.fasterxml.jackson.annotation.JsonAlias("vote_average")
    private Double voteAverage;

    @Transient
    @JsonProperty("genres")
    private List<GenreDto> genresList;

    @Transient
    @JsonProperty("genre_ids")
    private List<Integer> genreIds;

    public static class GenreDto {
        private Integer id;
        private String name;
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    private static final java.util.Map<Integer, String> GENRE_MAP = java.util.Map.ofEntries(
        java.util.Map.entry(28, "Acción"),
        java.util.Map.entry(12, "Aventura"),
        java.util.Map.entry(16, "Animación"),
        java.util.Map.entry(35, "Comedia"),
        java.util.Map.entry(80, "Crimen"),
        java.util.Map.entry(99, "Documental"),
        java.util.Map.entry(18, "Drama"),
        java.util.Map.entry(10751, "Familia"),
        java.util.Map.entry(14, "Fantasía"),
        java.util.Map.entry(36, "Historia"),
        java.util.Map.entry(27, "Terror"),
        java.util.Map.entry(10402, "Música"),
        java.util.Map.entry(9648, "Misterio"),
        java.util.Map.entry(10749, "Romance"),
        java.util.Map.entry(878, "Ciencia Ficción"),
        java.util.Map.entry(10770, "Película de TV"),
        java.util.Map.entry(53, "Suspense"),
        java.util.Map.entry(10752, "Bélica"),
        java.util.Map.entry(37, "Western")
    );

    @JsonProperty("genre")
    public String getGenre() {
        if (genresList != null && !genresList.isEmpty()) {
            return genresList.stream()
                    .map(GenreDto::getName)
                    .collect(java.util.stream.Collectors.joining(" / "));
        }
        if (genreIds != null && !genreIds.isEmpty()) {
            return genreIds.stream()
                    .map(id -> GENRE_MAP.getOrDefault(id, "Otros"))
                    .collect(java.util.stream.Collectors.joining(" / "));
        }
        return null;
    }

    @JsonProperty("rating")
    public Double getRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        int count = 0;
        for (Review r : reviews) {
            if (r.getRating() != null) {
                sum += r.getRating();
                count++;
            }
        }
        if (count == 0) return 0.0;
        return Math.round((sum / count) * 10.0) / 10.0;
    }

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

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
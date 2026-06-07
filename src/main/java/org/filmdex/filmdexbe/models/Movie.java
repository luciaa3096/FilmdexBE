package org.filmdex.filmdexbe.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "movies")
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

    @Transient
    @JsonProperty("credits")
    private CreditsDto credits;

    @Transient
    @JsonProperty("runtime")
    private Integer runtime;

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

    // Static DTOs
    public static class GenreDto {
        private Integer id;
        private String name;
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class CreditsDto {
        @JsonProperty("cast")
        private List<Person> castList;
        @JsonProperty("crew")
        private List<CrewMemberDto> crewList;
        
        public List<Person> getCastList() { return castList; }
        public void setCastList(List<Person> castList) { this.castList = castList; }
        public List<CrewMemberDto> getCrewList() { return crewList; }
        public void setCrewList(List<CrewMemberDto> crewList) { this.crewList = crewList; }
    }

    public static class CrewMemberDto {
        private Long id;
        private String name;
        private String job;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getJob() { return job; }
        public void setJob(String job) { this.job = job; }
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

    @JsonProperty("duration")
    public String getDuration() {
        if (runtime != null) {
            return runtime + " min";
        }
        return null;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public List<GenreDto> getGenresList() {
        return genresList;
    }

    public void setGenresList(List<GenreDto> genresList) {
        this.genresList = genresList;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public CreditsDto getCredits() {
        return credits;
    }

    public void setCredits(CreditsDto credits) {
        this.credits = credits;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Person getDirector() {
        if (this.director != null) {
            return this.director;
        }
        if (credits != null && credits.getCrewList() != null) {
            for (CrewMemberDto crew : credits.getCrewList()) {
                if ("Director".equalsIgnoreCase(crew.getJob())) {
                    Person p = new Person();
                    p.setId(crew.getId());
                    p.setName(crew.getName());
                    return p;
                }
            }
        }
        return null;
    }

    public void setDirector(Person director) {
        this.director = director;
    }

    public List<Person> getCast() {
        if (this.cast != null && !this.cast.isEmpty()) {
            return this.cast;
        }
        if (credits != null && credits.getCastList() != null) {
            return credits.getCastList().stream()
                    .limit(6)
                    .collect(java.util.stream.Collectors.toList());
        }
        return this.cast;
    }

    public void setCast(List<Person> cast) {
        this.cast = cast;
    }

    @JsonProperty("castMembers")
    public List<Person> getCastMembers() {
        return getCast();
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
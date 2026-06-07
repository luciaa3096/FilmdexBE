package org.filmdex.filmdexbe.controllers;

import org.filmdex.filmdexbe.models.User;
import org.filmdex.filmdexbe.models.Movie;
import org.filmdex.filmdexbe.services.UserService;
import org.filmdex.filmdexbe.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/watched")
    public ResponseEntity<List<Movie>> getWatchedMovies(@PathVariable Long userId) {
        List<Long> movieIds = userService.getWatchedMovies(userId);

        List<Movie> movies = movieIds.stream()
                .map(id -> movieService.getMovieById(id))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return ResponseEntity.ok(movies);
    }

    @PostMapping("/{userId}/watched/{movieId}")
    public ResponseEntity<Void> markAsWatched(@PathVariable Long userId, @PathVariable Long movieId) {
        userService.addMovieToWatched(userId, movieId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/watched/{movieId}")
    public ResponseEntity<Void> removeFromWatched(@PathVariable Long userId, @PathVariable Long movieId) {
        userService.removeMovieFromWatched(userId, movieId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/favourites")
    public ResponseEntity<List<Movie>> getFavouriteMovies(@PathVariable Long userId) {
        List<Long> movieIds = userService.getFavouriteMovies(userId);

        List<Movie> movies = movieIds.stream()
                .map(id -> movieService.getMovieById(id))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return ResponseEntity.ok(movies);
    }

    @PostMapping("/{userId}/favourites/{movieId}")
    public ResponseEntity<Void> addToFavourites(@PathVariable Long userId, @PathVariable Long movieId) {
        userService.addMovieToFavourites(userId, movieId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/favourites/{movieId}")
    public ResponseEntity<Void> removeFromFavourites(@PathVariable Long userId, @PathVariable Long movieId) {
        userService.removeMovieFromFavourites(userId, movieId);
        return ResponseEntity.ok().build();
    }
}
package org.filmdex.filmdexbe.controllers;

import org.filmdex.filmdexbe.models.User;
import org.filmdex.filmdexbe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


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

    // Obtener los IDs de las películas vistas de un usuario
    @GetMapping("/{userId}/watched")
    public ResponseEntity<List<Long>> getWatchedMovies(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getWatchedMovies(userId));
    }

    // Añadir película a vistas
    @PostMapping("/{userId}/watched/{movieId}")
    public ResponseEntity<Void> markAsWatched(@PathVariable Long userId, @PathVariable Long movieId) {
        userService.addMovieToWatched(userId, movieId);
        return ResponseEntity.ok().build();
    }

    // Eliminar película de vistas
    @DeleteMapping("/{userId}/watched/{movieId}")
    public ResponseEntity<Void> removeFromWatched(@PathVariable Long userId, @PathVariable Long movieId) {
        userService.removeMovieFromWatched(userId, movieId);
        return ResponseEntity.ok().build();
    }

    // Obtener los IDs de las películas favoritas de un usuario
    @GetMapping("/{userId}/favourites")
    public ResponseEntity<List<Long>> getFavouriteMovies(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getFavouriteMovies(userId));
    }

    // Añadir película a favoritos
    @PostMapping("/{userId}/favourites/{movieId}")
    public ResponseEntity<Void> addToFavourites(@PathVariable Long userId, @PathVariable Long movieId) {
        userService.addMovieToFavourites(userId, movieId);
        return ResponseEntity.ok().build();
    }

    // Eliminar película de favoritos
    @DeleteMapping("/{userId}/favourites/{movieId}")
    public ResponseEntity<Void> removeFromFavourites(@PathVariable Long userId, @PathVariable Long movieId) {
        userService.removeMovieFromFavourites(userId, movieId);
        return ResponseEntity.ok().build();
    }
}
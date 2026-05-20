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
        return ResponseEntity.ok(userService.getUserById(id));
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


    @PostMapping("/{userId}/watched/{movieId}")
    public ResponseEntity<User> markAsWatched(@PathVariable Long userId, @PathVariable Long movieId) {
        return ResponseEntity.ok(userService.addMovieToWatched(userId, movieId));
    }


    @PostMapping("/{userId}/favourites/{movieId}")
    public ResponseEntity<User> addToFavourites(@PathVariable Long userId, @PathVariable Long movieId) {
        return ResponseEntity.ok(userService.addMovieToFavourites(userId, movieId));
    }

    @DeleteMapping("/{userId}/favourites/{movieId}")
    public ResponseEntity<User> removeFromFavourites(@PathVariable Long userId, @PathVariable Long movieId) {
        return ResponseEntity.ok(userService.removeMovieFromFavourites(userId, movieId));
    }
}
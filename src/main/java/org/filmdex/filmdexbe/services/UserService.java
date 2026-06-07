package org.filmdex.filmdexbe.services;

import org.filmdex.filmdexbe.models.User;
import org.filmdex.filmdexbe.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return (User) userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) {
        if (user.getId() != null) {
            User existingUser = userRepository.findById(user.getId()).orElse(null);
            if (existingUser != null) {
                if (user.getName() != null) {
                    existingUser.setName(user.getName());
                }
                if (user.getUsername() != null) {
                    existingUser.setUsername(user.getUsername());
                }
                if (user.getPassword() != null) {
                    existingUser.setPassword(user.getPassword());
                }
                return userRepository.save(existingUser);
            }
        }
        return userRepository.save(user);
    }

    // Obtiene los IDs de las películas favoritas
    public List<Long> getFavouriteMovies(Long userId) {
        return userRepository.findFavouriteMoviesByUserId(userId);
    }

    // Añade la película a favoritas
    public void addMovieToFavourites(Long userId, Long movieId) {
        userRepository.addMovieToFavourites(userId, movieId);
    }

    // Elimina la película de favorita
    public void removeMovieFromFavourites(Long userId, Long movieId) {
        userRepository.removeMovieFromFavourites(userId, movieId);
    }

    // Obtiene los IDs de las películas vistas
    public List<Long> getWatchedMovies(Long userId) {
        return userRepository.findWatchedMoviesByUserId(userId);
    }

    // Añade la película a vistas
    public void addMovieToWatched(Long userId, Long movieId) {
        userRepository.addMovieToWatched(userId, movieId);
    }

    // Elimina la película de vistas
    public void removeMovieFromWatched(Long userId, Long movieId) {
        userRepository.removeMovieFromWatched(userId, movieId);
    }
}
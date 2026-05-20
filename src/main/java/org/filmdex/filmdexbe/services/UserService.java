package org.filmdex.filmdexbe.services;

import jakarta.transaction.Transactional;
import org.filmdex.filmdexbe.models.Movie;
import org.filmdex.filmdexbe.models.User;
import org.filmdex.filmdexbe.repositories.MovieRepository;
import org.filmdex.filmdexbe.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User addMovieToWatched(Long userId, Long movieId) {
        User user = getUserById(userId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Película no encontrada con id: " + movieId));

        // Evita duplicados en la lista antes de guardar
        if (!user.getWatched().contains(movie)) {
            user.getWatched().add(movie);
        }

        return userRepository.save(user);
    }

    /**
     * Agrega una película a la lista de "favoritas" de un usuario
     */
    @Transactional
    public User addMovieToFavourites(Long userId, Long movieId) {
        User user = getUserById(userId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Película no encontrada con id: " + movieId));

        if (!user.getFavourites().contains(movie)) {
            user.getFavourites().add(movie);
        }

        return userRepository.save(user);
    }

    /**
     * Elimina una película de la lista de favoritas de un usuario
     */
    @Transactional
    public User removeMovieFromFavourites(Long userId, Long movieId) {
        User user = getUserById(userId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Película no encontrada con id: " + movieId));

        user.getFavourites().remove(movie);
        return userRepository.save(user);
    }
}
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

    public List<Long> getFavouriteMovies(Long userId) {
        return userRepository.findFavouriteMoviesByUserId(userId);
    }

    public void addMovieToFavourites(Long userId, Long movieId) {
        userRepository.addMovieToFavourites(userId, movieId);
    }

    public void removeMovieFromFavourites(Long userId, Long movieId) {
        userRepository.removeMovieFromFavourites(userId, movieId);
    }

    public List<Long> getWatchedMovies(Long userId) {
        return userRepository.findWatchedMoviesByUserId(userId);
    }

    public void addMovieToWatched(Long userId, Long movieId) {
        userRepository.addMovieToWatched(userId, movieId);
    }

    public void removeMovieFromWatched(Long userId, Long movieId) {
        userRepository.removeMovieFromWatched(userId, movieId);
    }
}
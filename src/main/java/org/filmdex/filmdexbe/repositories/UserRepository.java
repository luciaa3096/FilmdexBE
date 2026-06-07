package org.filmdex.filmdexbe.repositories;

import org.filmdex.filmdexbe.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Obtener los IDs de las películas favoritas
    @Query(value = "SELECT movie_id FROM user_favourite_movies WHERE user_id = :userId", nativeQuery = true)
    List<Long> findFavouriteMoviesByUserId(@Param("userId") Long userId);

    // Añadir película a favoritos
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_favourite_movies (user_id, movie_id) " +
            "SELECT :userId, :movieId FROM DUAL " +
            "WHERE NOT EXISTS (SELECT 1 FROM user_favourite_movies WHERE user_id = :userId AND movie_id = :movieId)",
            nativeQuery = true)
    void addMovieToFavourites(@Param("userId") Long userId, @Param("movieId") Long movieId);

    // Eliminar película de favoritos
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_favourite_movies WHERE user_id = :userId AND movie_id = :movieId", nativeQuery = true)
    void removeMovieFromFavourites(@Param("userId") Long userId, @Param("movieId") Long movieId);

    // Obtener los IDs de las películas vistas
    @Query(value = "SELECT movie_id FROM user_watched_movies WHERE user_id = :userId", nativeQuery = true)
    List<Long> findWatchedMoviesByUserId(@Param("userId") Long userId);

    // Añadir película a vistas (Evita duplicados)
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_watched_movies (user_id, movie_id) " +
            "SELECT :userId, :movieId FROM DUAL " +
            "WHERE NOT EXISTS (SELECT 1 FROM user_watched_movies WHERE user_id = :userId AND movie_id = :movieId)",
            nativeQuery = true)
    void addMovieToWatched(@Param("userId") Long userId, @Param("movieId") Long movieId);

    // Eliminar película de vistas
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_watched_movies WHERE user_id = :userId AND movie_id = :movieId", nativeQuery = true)
    void removeMovieFromWatched(@Param("userId") Long userId, @Param("movieId") Long movieId);
}
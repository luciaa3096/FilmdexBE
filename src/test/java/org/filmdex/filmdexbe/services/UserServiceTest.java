package org.filmdex.filmdexbe.services;

import org.filmdex.filmdexbe.models.Movie;
import org.filmdex.filmdexbe.models.User;
import org.filmdex.filmdexbe.repositories.MovieRepository;
import org.filmdex.filmdexbe.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita el soporte de Mockito en JUnit 5
public class UserServiceTest {

    @Mock
    private UserRepository userRepository; // Crea un doble simulado del repositorio

    @Mock
    private MovieRepository movieRepository; // Crea un doble simulado del repositorio de películas

    @InjectMocks
    private UserService userService; // Inyecta automáticamente los @Mock dentro del servicio

    private User sampleUser;
    private Movie sampleMovie;

    @BeforeEach
    void setUp() {
        // Inicializamos objetos limpios para cada test
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setName("Test User");
        sampleUser.setUsername("testuser");
        sampleUser.setWatched(new ArrayList<>());
        sampleUser.setFavourites(new ArrayList<>());

        sampleMovie = new Movie();
        sampleMovie.setId(5L);
        sampleMovie.setTitle("Inception");
    }

    @Test
    void testGetUserById_Success() {
        // Configuración del comportamiento del Mock
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        // Ejecución
        User result = userService.getUserById(1L);

        // Verificaciones (Assertions)
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findById(1L); // Verifica que se llamó exactamente 1 vez
    }

    @Test
    void testGetUserById_NotFound() {
        // Cuando no se encuentra, tu servicio devuelve null
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        User result = userService.getUserById(2L);

        assertNull(result);
    }

    @Test
    void testAddMovieToWatched_Success() {
        // Simulamos que tanto el usuario como la película existen
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(movieRepository.findById(5L)).thenReturn(Optional.of(sampleMovie));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        // Ejecución
        User updatedUser = userService.addMovieToWatched(1L, 5L);

        // Verificamos que la película se añadió a la lista "watched"
        assertNotNull(updatedUser);
        assertTrue(updatedUser.getWatched().contains(sampleMovie));
        assertEquals(1, updatedUser.getWatched().size());

        // Comprobamos que se invocó el guardado en el repositorio
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void testAddMovieToWatched_AvoidDuplicates() {
        // Añadimos previamente la película para simular que ya la vio
        sampleUser.getWatched().add(sampleMovie);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(movieRepository.findById(5L)).thenReturn(Optional.of(sampleMovie));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        // Ejecución: Intentamos añadirla de nuevo
        User updatedUser = userService.addMovieToWatched(1L, 5L);

        // Validamos que NO se duplicó gracias a la lógica de tu Service
        assertEquals(1, updatedUser.getWatched().size());
    }

    @Test
    void testAddMovieToWatched_MovieNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        // Simulamos que la película con ID 99 no existe en la base de datos
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        // Verificamos que el servicio lance la excepción RuntimeException esperada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.addMovieToWatched(1L, 99L);
        });

        assertEquals("Película no encontrada con id: 99", exception.getMessage());
        // Nos aseguramos de que el repositorio nunca intentó guardar un usuario con datos erróneos
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAddMovieToFavourites_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(movieRepository.findById(5L)).thenReturn(Optional.of(sampleMovie));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        User updatedUser = userService.addMovieToFavourites(1L, 5L);

        assertTrue(updatedUser.getFavourites().contains(sampleMovie));
    }

    @Test
    void testRemoveMovieFromFavourites() {
        // Empezamos con la película ya añadida a favoritos
        sampleUser.getFavourites().add(sampleMovie);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(movieRepository.findById(5L)).thenReturn(Optional.of(sampleMovie));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        // Ejecución
        User updatedUser = userService.removeMovieFromFavourites(1L, 5L);

        // Verificamos que se removió con éxito
        assertFalse(updatedUser.getFavourites().contains(sampleMovie));
        assertTrue(updatedUser.getFavourites().isEmpty());
    }
}
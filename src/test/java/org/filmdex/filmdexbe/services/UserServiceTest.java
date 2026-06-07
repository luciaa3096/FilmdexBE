package org.filmdex.filmdexbe.services;

import org.filmdex.filmdexbe.models.User;
import org.filmdex.filmdexbe.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setName("Andres Garcia");
        sampleUser.setUsername("andres");
        sampleUser.setPassword("123");
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);

        User result = userService.createUser(sampleUser);

        assertNotNull(result);
        assertEquals("andres", result.getUsername());
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("Andres Garcia", result.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        User result = userService.getUserById(2L);

        assertNull(result);
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("andres", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);

        User result = userService.saveUser(sampleUser);

        assertNotNull(result);
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void testGetFavouriteMovies() {
        List<Long> expectedMovieIds = List.of(10L, 20L);
        when(userRepository.findFavouriteMoviesByUserId(1L)).thenReturn(expectedMovieIds);

        List<Long> result = userService.getFavouriteMovies(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(10L));
        verify(userRepository, times(1)).findFavouriteMoviesByUserId(1L);
    }

    @Test
    void testAddMovieToFavourites() {
        doNothing().when(userRepository).addMovieToFavourites(1L, 10L);

        userService.addMovieToFavourites(1L, 10L);

        verify(userRepository, times(1)).addMovieToFavourites(1L, 10L);
    }

    @Test
    void testRemoveMovieFromFavourites() {
        doNothing().when(userRepository).removeMovieFromFavourites(1L, 10L);

        userService.removeMovieFromFavourites(1L, 10L);

        verify(userRepository, times(1)).removeMovieFromFavourites(1L, 10L);
    }

    @Test
    void testGetWatchedMovies() {
        List<Long> expectedMovieIds = List.of(30L);
        when(userRepository.findWatchedMoviesByUserId(1L)).thenReturn(expectedMovieIds);

        List<Long> result = userService.getWatchedMovies(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(30L));
        verify(userRepository, times(1)).findWatchedMoviesByUserId(1L);
    }

    @Test
    void testAddMovieToWatched() {
        doNothing().when(userRepository).addMovieToWatched(1L, 30L);

        userService.addMovieToWatched(1L, 30L);

        verify(userRepository, times(1)).addMovieToWatched(1L, 30L);
    }

    @Test
    void testRemoveMovieFromWatched() {
        doNothing().when(userRepository).removeMovieFromWatched(1L, 30L);

        userService.removeMovieFromWatched(1L, 30L);

        verify(userRepository, times(1)).removeMovieFromWatched(1L, 30L);
    }
}
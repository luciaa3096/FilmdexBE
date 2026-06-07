package org.filmdex.filmdexbe.services;

import org.filmdex.filmdexbe.models.Movie;
import org.filmdex.filmdexbe.models.Person;
import org.filmdex.filmdexbe.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private Person directorPerson;
    private Person actorPerson;

    @BeforeEach
    void setUp() {
        directorPerson = new Person();
        directorPerson.setId(1L);
        directorPerson.setName("Christopher Nolan");
        Movie movie = new Movie();
        directorPerson.setDirectedMovies(List.of(movie));

        actorPerson = new Person();
        actorPerson.setId(2L);
        actorPerson.setName("Leonardo DiCaprio");
        actorPerson.setDirectedMovies(new ArrayList<>());
    }

    @Test
    void testGetPersonById_Success() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(directorPerson));

        Person result = personService.getPersonById(1L);

        assertNotNull(result);
        assertEquals("Christopher Nolan", result.getName());
    }

    @Test
    void testGetPersonById_NotFound_ThrowsException() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            personService.getPersonById(99L);
        });

        assertEquals("Persona no encontrada con id: 99", exception.getMessage());
    }

    @Test
    void testDeletePerson_Success() {
        when(personRepository.existsById(1L)).thenReturn(true);
        doNothing().when(personRepository).deleteById(1L);

        personService.deletePerson(1L);

        verify(personRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePerson_NotFound_ThrowsException() {
        when(personRepository.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            personService.deletePerson(99L);
        });

        assertEquals("No se puede borrar: Persona no encontrada", exception.getMessage());
        verify(personRepository, never()).deleteById(anyLong());
    }


}
package g4vr3.bibliotecaapi.controller;

import g4vr3.bibliotecaapi.model.Libro;
import g4vr3.bibliotecaapi.repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LibroControllerTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroController libroController;

    private Libro libro;

    @BeforeEach
    public void setUp() {
        libro = new Libro();
        libro.setIsbn("12345");
        libro.setTitulo("Java para todos");
        libro.setAutor("Juan Pérez");
    }

    @Test
    public void testGetAllLibros() {
        // Simula una lista de libros
        when(libroRepository.findAll()).thenReturn(List.of(libro));

        ResponseEntity<List<Libro>> response = libroController.getAllLibros();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testGetLibro() {
        when(libroRepository.findById("12345")).thenReturn(Optional.of(libro));

        ResponseEntity<?> response = libroController.getLibro("12345");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetLibroNotFound() {
        when(libroRepository.findById("12345")).thenReturn(Optional.empty());

        ResponseEntity<?> response = libroController.getLibro("12345");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testPostLibro() {
        when(libroRepository.existsById("12345")).thenReturn(false);
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);

        ResponseEntity<?> response = libroController.postLibro(libro);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testPostLibroConflict() {
        when(libroRepository.existsById("12345")).thenReturn(true);

        ResponseEntity<?> response = libroController.postLibro(libro);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testUpdateLibro() {
        when(libroRepository.existsById("12345")).thenReturn(true);
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);

        ResponseEntity<?> response = libroController.updateLibro("12345", libro);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateLibroNotFound() {
        when(libroRepository.existsById("12345")).thenReturn(false);

        ResponseEntity<?> response = libroController.updateLibro("12345", libro);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteLibro() {
        when(libroRepository.existsById("12345")).thenReturn(true);

        ResponseEntity<?> response = libroController.deleteLibro("12345");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteLibroNotFound() {
        when(libroRepository.existsById("12345")).thenReturn(false);

        ResponseEntity<?> response = libroController.deleteLibro("12345");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

package g4vr3.bibliotecaapi.controller;

import g4vr3.bibliotecaapi.model.Ejemplar;
import g4vr3.bibliotecaapi.model.Libro;
import g4vr3.bibliotecaapi.repository.EjemplarRepository;
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
public class EjemplarControllerTest {

    @Mock
    private EjemplarRepository ejemplarRepository;

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private EjemplarController ejemplarController;

    private Ejemplar ejemplar;
    private Libro libro;

    @BeforeEach
    public void setUp() {
        libro = new Libro();
        libro.setIsbn("12345");
        libro.setTitulo("Java para todos");
        libro.setAutor("Juan Pérez");

        ejemplar = new Ejemplar();
        ejemplar.setId(1);
        ejemplar.setLibro(libro);
        ejemplar.setEstado("Disponible");
    }

    @Test
    public void testGetAllEjemplar() {
        when(ejemplarRepository.findAll()).thenReturn(List.of(ejemplar));

        ResponseEntity<?> response = ejemplarController.getAllEjemplar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    public void testGetEjemplar() {
        when(ejemplarRepository.findById(1)).thenReturn(Optional.of(ejemplar));

        ResponseEntity<?> response = ejemplarController.getEjemplar(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetEjemplarNotFound() {
        when(ejemplarRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = ejemplarController.getEjemplar(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testPostEjemplar() {
        when(libroRepository.existsById(libro.getIsbn())).thenReturn(true);
        when(ejemplarRepository.save(any(Ejemplar.class))).thenReturn(ejemplar);

        ResponseEntity<?> response = ejemplarController.postEjemplar(ejemplar);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testPostEjemplarNotFound() {
        when(libroRepository.existsById(libro.getIsbn())).thenReturn(false);

        ResponseEntity<?> response = ejemplarController.postEjemplar(ejemplar);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteEjemplar() {
        when(ejemplarRepository.existsById(1)).thenReturn(true);

        ResponseEntity<?> response = ejemplarController.deleteEjemplar(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteEjemplarNotFound() {
        when(ejemplarRepository.existsById(1)).thenReturn(false);

        ResponseEntity<?> response = ejemplarController.deleteEjemplar(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

package g4vr3.bibliotecaapi.controller;

import g4vr3.bibliotecaapi.model.Prestamo;
import g4vr3.bibliotecaapi.model.Usuario;
import g4vr3.bibliotecaapi.model.Ejemplar;
import g4vr3.bibliotecaapi.repository.PrestamoRepository;
import g4vr3.bibliotecaapi.repository.UsuarioRepository;
import g4vr3.bibliotecaapi.repository.EjemplarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PrestamoControllerTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EjemplarRepository ejemplarRepository;

    @InjectMocks
    private PrestamoController prestamoController;

    private Prestamo prestamo;
    private Usuario usuario;
    private Ejemplar ejemplar;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Juan Pérez");

        ejemplar = new Ejemplar();
        ejemplar.setId(1);

        prestamo = new Prestamo();
        prestamo.setId(1);
        prestamo.setUsuario(usuario);
        prestamo.setEjemplar(ejemplar);
        prestamo.setFechaInicio(LocalDate.of(2025, 1, 1));
        prestamo.setFechaDevolucion(LocalDate.of(2025, 1, 10));
    }

    @Test
    public void testGetAllPrestamo() {
        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo));

        ResponseEntity<?> response = prestamoController.getAllPrestamo();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    public void testGetPrestamo() {
        when(prestamoRepository.findById(1)).thenReturn(Optional.of(prestamo));

        ResponseEntity<?> response = prestamoController.getPrestamo(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetPrestamoNotFound() {
        when(prestamoRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = prestamoController.getPrestamo(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testPostPrestamo() {
        when(prestamoRepository.existsById(prestamo.getId())).thenReturn(false);
        when(prestamoRepository.save(any(Prestamo.class))).thenReturn(prestamo);

        ResponseEntity<?> response = prestamoController.postPrestamo(prestamo);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testPostPrestamoConflict() {
        when(prestamoRepository.existsById(prestamo.getId())).thenReturn(true);

        ResponseEntity<?> response = prestamoController.postPrestamo(prestamo);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testUpdatePrestamo() {
        when(prestamoRepository.existsById(prestamo.getId())).thenReturn(true);
        when(prestamoRepository.save(any(Prestamo.class))).thenReturn(prestamo);

        ResponseEntity<?> response = prestamoController.updatePrestamo(prestamo, prestamo.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdatePrestamoNotFound() {
        when(prestamoRepository.existsById(prestamo.getId())).thenReturn(false);

        ResponseEntity<?> response = prestamoController.updatePrestamo(prestamo, prestamo.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeletePrestamo() {
        when(prestamoRepository.existsById(1)).thenReturn(true);

        ResponseEntity<String> response = prestamoController.deletePrestamo(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeletePrestamoNotFound() {
        when(prestamoRepository.existsById(1)).thenReturn(false);

        ResponseEntity<String> response = prestamoController.deletePrestamo(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
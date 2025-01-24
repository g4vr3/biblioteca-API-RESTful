package g4vr3.bibliotecaapi.controller;

import g4vr3.bibliotecaapi.model.Usuario;
import g4vr3.bibliotecaapi.repository.UsuarioRepository;
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
public class UsuarioControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setDni("12345678A");
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan.perez@example.com");
        usuario.setPassword("password123");
        usuario.setTipo("ADMIN");
        usuario.setPenalizacionHasta(LocalDate.of(2025, 1, 1));
    }

    @Test
    public void testGetAllUsuario() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        ResponseEntity<List<Usuario>> response = usuarioController.getAllUsuario();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testGetUsuario() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        ResponseEntity<?> response = usuarioController.getUsuario(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetUsuarioNotFound() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = usuarioController.getUsuario(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error: No se encontró un usuario con el ID 1", response.getBody());
    }

    @Test
    public void testPostUsuario() {
        when(usuarioRepository.existsById(usuario.getId())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<?> response = usuarioController.postUsuario(usuario);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testPostUsuarioConflict() {
        when(usuarioRepository.existsById(usuario.getId())).thenReturn(true);

        ResponseEntity<?> response = usuarioController.postUsuario(usuario);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Error: Ya existe un usuario con el ID 1", response.getBody());
    }

    @Test
    public void testUpdateUsuario() {
        when(usuarioRepository.existsById(usuario.getId())).thenReturn(true);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<?> response = usuarioController.updateUsuario(usuario, usuario.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateUsuarioNotFound() {
        when(usuarioRepository.existsById(usuario.getId())).thenReturn(false);

        ResponseEntity<?> response = usuarioController.updateUsuario(usuario, usuario.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error: No se encontró un usuario con el ID 1", response.getBody());
    }

    @Test
    public void testDeleteUsuario() {
        when(usuarioRepository.existsById(1)).thenReturn(true);

        ResponseEntity<?> response = usuarioController.deleteUsuario(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Eliminado correctamente", response.getBody());
    }

    @Test
    public void testDeleteUsuarioNotFound() {
        when(usuarioRepository.existsById(1)).thenReturn(false);

        ResponseEntity<?> response = usuarioController.deleteUsuario(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error: No se encontró un usuario con el ID 1", response.getBody());
    }
}
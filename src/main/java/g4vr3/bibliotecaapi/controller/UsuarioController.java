package g4vr3.bibliotecaapi.controller;

import g4vr3.bibliotecaapi.model.Usuario;
import g4vr3.bibliotecaapi.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/usuarios")
@CacheConfig(cacheNames = {"usuarios"})
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuario() {
        List<Usuario> usuarios = this.usuarioRepository.findAll();

        // Verificar si la lista está vacía
        if (usuarios.isEmpty())
            return ResponseEntity.noContent().build(); // 204 NO CONTENT

        return ResponseEntity.ok(usuarios); // 200 OK + Usuarios
    }

    @GetMapping("/{id}")
    @Cacheable
    public ResponseEntity<?> getUsuario(@PathVariable int id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario != null) {
            return ResponseEntity.ok(usuario); // 200 OK + usuario
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un usuario con el ID " + id); // 404 NOT FOUND
        }
    }

    @PostMapping
    public ResponseEntity<?> postUsuario(@Valid @RequestBody Usuario usuarioToPost) {
        // Si el usuario a crear ya existe, 409 CONFLICT
        if (usuarioRepository.existsById(usuarioToPost.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: Ya existe un usuario con el ID " + usuarioToPost.getId());
        }

        Usuario postedUsuario = usuarioRepository.save(usuarioToPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(postedUsuario); // 201 CREATED
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@RequestBody Usuario usuarioToUpdate, @PathVariable int id) {
        // Si no existe el usuario a actualizar, 404 NOT FOUND
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un usuario con el ID " + id);
        }

        // Si el ID del path y el body no coincide, 400 BAD REQUEST
        if (!Objects.equals(usuarioToUpdate.getId(), id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: El ID del cuerpo de la solicitud no coincide con el ID de la URL.");
        }

        // Guardar el usuario actualizado
        Usuario updatedUsuario = usuarioRepository.save(usuarioToUpdate);
        return ResponseEntity.ok(updatedUsuario); // 200 OK + usuario
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable int id) {
        // Si el usuario a eliminar no existe, 404 NOT FOUND
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un usuario con el ID " + id);
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Eliminado correctamente"); // 204 NO CONTENT
    }
}
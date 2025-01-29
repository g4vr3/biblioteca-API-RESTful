package g4vr3.bibliotecaapi.controller;

import g4vr3.bibliotecaapi.model.Prestamo;
import g4vr3.bibliotecaapi.repository.EjemplarRepository;
import g4vr3.bibliotecaapi.repository.PrestamoRepository;
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
@RequestMapping("/prestamos")
@CacheConfig(cacheNames = {"prestamos"})
public class PrestamoController {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EjemplarRepository ejemplarRepository;

    @Autowired
    public PrestamoController(PrestamoRepository prestamoRepository, UsuarioRepository usuarioRepository, EjemplarRepository ejemplarRepository) {
        this.prestamoRepository = prestamoRepository;
        this.usuarioRepository = usuarioRepository;
        this.ejemplarRepository = ejemplarRepository;
    }
    //GET --> SELECT *
    @GetMapping
    public ResponseEntity<?> getAllPrestamo(){
        List<Prestamo> prestamos = this.prestamoRepository.findAll();

        // Verificar si la lista está vacía
        if (prestamos.isEmpty())
            return ResponseEntity.noContent().build(); // 204 NO CONTENT

        return ResponseEntity.ok(prestamos); // 200 OK + Prestamos
    }

    //GET BY ID --> SELECT BY ID
    @GetMapping("/{id}")
    @Cacheable
    public ResponseEntity<?> getPrestamo(@PathVariable int id){
        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);

        if (prestamo != null) {
            return ResponseEntity.ok(prestamo); // 200 OK + préstamo
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un préstamo con el ID " + id); // 404 NOT FOUND
        }
    }

    //POST --> INSERT
    @PostMapping
    public ResponseEntity<?> postPrestamo(@Valid @RequestBody Prestamo prestamoToPost){
        // Si no existe el usuario asociado al préstamo, 404 NOT FOUND
        if (!usuarioRepository.existsById(prestamoToPost.getUsuario().getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un usuario con el ID " + prestamoToPost.getUsuario().getId());
        }

        // Si no existe el ejemplar asociado al préstamo, 404 NOT FOUND
        if (!ejemplarRepository.existsById(prestamoToPost.getEjemplar().getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un ejemplar con el ID " + prestamoToPost.getEjemplar().getId());
        }

        // Si el préstamo a crear ya existe, 409 CONFLICT
        if (prestamoRepository.existsById(prestamoToPost.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: Ya existe un préstamo con el ID " + prestamoToPost.getId());
        }
        Prestamo postedPrestamo = prestamoRepository.save(prestamoToPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(postedPrestamo); // 201 CREATED
    }

    //PUT --> UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrestamo(@RequestBody Prestamo prestamoToUpdate, @PathVariable int id){
        // Si no existe el préstamo a actualizar, 404 NOT FOUND
        if (!prestamoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un préstamo con el ID " + id);
        }
        // Si el ISBN del path y el body no coincide, 400 BAD REQUEST
        if (!Objects.equals(prestamoToUpdate.getId(), id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: El ID del cuerpo de la solicitud no coincide con el ID de la URL.");
        }

        // Guardar el préstamo actualizado
        Prestamo updatedPrestamo = prestamoRepository.save(prestamoToUpdate);
        return ResponseEntity.ok(updatedPrestamo); // 200 OK + préstamo
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePrestamo(@PathVariable int id){
        // Si el préstamo a eliminar no existe, 404 NOT FOUND
        if (!prestamoRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un préstamo con el ID " + id);
        }
        prestamoRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Eliminado correctamente"); // 204 NO CONTENT
    }
}

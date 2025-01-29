package g4vr3.bibliotecaapi.controller;

import g4vr3.bibliotecaapi.model.Ejemplar;
import g4vr3.bibliotecaapi.repository.EjemplarRepository;
import g4vr3.bibliotecaapi.repository.LibroRepository;
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
@RequestMapping("/ejemplares")
@CacheConfig(cacheNames = {"ejemplares"})
public class EjemplarController {

    private final EjemplarRepository ejemplarRepository;
    private final LibroRepository libroRepository;

    @Autowired
    public EjemplarController(EjemplarRepository ejemplarRepository, LibroRepository libroRepository) {
        this.ejemplarRepository = ejemplarRepository;
        this.libroRepository = libroRepository;
    }

    //GET --> SELECT ALL
    @GetMapping
    public ResponseEntity<?> getAllEjemplar() {
        List<Ejemplar> ejemplares = this.ejemplarRepository.findAll();

        // Verificar si la lista está vacía
        if (ejemplares.isEmpty())
            return ResponseEntity.noContent().build(); // 204 NO CONTENT

        return ResponseEntity.ok(ejemplares); // 200 OK + Ejemplares
    }

    // GET BY ID --> Obtener un libro por su ID
    @GetMapping("/{id}")
    @Cacheable
    public ResponseEntity<?> getEjemplar(@PathVariable int id) {
        return this.ejemplarRepository.findById(id)
                .map(ResponseEntity::ok) // 200 OK + Ejemplar
                .orElse(ResponseEntity.notFound().build()); // 404 NOT FOUND
    }

    @PostMapping
    public ResponseEntity<?> postEjemplar(@Valid @RequestBody Ejemplar ejemplarToPost) {
        // Verificar si el libro asociado al ejemplar existe
        if (!this.libroRepository.existsById(ejemplarToPost.getLibro().getIsbn())) {
            // Si el libro no existe, 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: El libro con el ISBN " + ejemplarToPost.getLibro().getIsbn() + " no existe.");
        }

        // Guardar el ejemplar si el libro existe
        Ejemplar postedEjemplar = this.ejemplarRepository.save(ejemplarToPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(postedEjemplar); // 201 CREATED
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEjemplar(@PathVariable int id, @RequestBody Ejemplar ejemplarToUpdate) {
        // Si no existe el ejemplar a actualizar, 404 NOT FOUND
        if (!this.ejemplarRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un ejemplar con el ID " + id);
        }

        // Si el ID del path y del body no coinciden, 400 BAD REQUEST
        if (!Objects.equals(ejemplarToUpdate.getId(), id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: El ID del cuerpo de la solicitud no coincide con el ID de la URL.");
        }

        Ejemplar updatedEjemplar = this.ejemplarRepository.save(ejemplarToUpdate);
        return ResponseEntity.ok(updatedEjemplar); // 200 OK + libro
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEjemplar(@PathVariable int id){
        // Si no existe el ejemplar a eliminar, 404 NOT FOUND
        if (!this.ejemplarRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un ejemplar con el ID " + id);
        }
        this.ejemplarRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Eliminado correctamente"); // 204 NO CONTENT
    }

}

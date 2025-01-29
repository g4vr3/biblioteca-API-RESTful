package g4vr3.bibliotecaapi.controller;

import g4vr3.bibliotecaapi.model.Libro;
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
@RequestMapping("/libros")
@CacheConfig(cacheNames = {"libros"})
public class LibroController {

    private final LibroRepository libroRepository;

    @Autowired
    public LibroController(LibroRepository libroRepository){this.libroRepository = libroRepository;}

    // GET --> Obtener todos los libros
    @GetMapping
    public ResponseEntity<List<Libro>> getAllLibros() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty())
            return ResponseEntity.noContent().build(); // 204 NO CONTENT

        return ResponseEntity.ok(libros); // 200 OK + libros
    }

    // GET BY ISBN --> Obtener un libro por su ISBN
    @GetMapping("/{isbn}")
    @Cacheable
    public ResponseEntity<?> getLibro(@PathVariable String isbn) {
        Libro libro = libroRepository.findById(isbn).orElse(null);

        if (libro != null) {
            return ResponseEntity.ok(libro); // 200 OK + libro
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un libro con el ISBN " + isbn); // 404 NOT FOUND
        }
    }

    // POST --> Crear un nuevo libro
    @PostMapping
    public ResponseEntity<?> postLibro(@Valid @RequestBody Libro libroToPost) {
        // Si el libro a crear ya existe, 409 CONFLICT
        if (libroRepository.existsById(libroToPost.getIsbn())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: Ya existe un libro con el ISBN " + libroToPost.getIsbn());
        }
        Libro postedLibro = libroRepository.save(libroToPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(postedLibro); // 201 CREATED
    }

    // PUT --> Actualizar un libro existente
    @PutMapping("/{isbn}")
    public ResponseEntity<?> updateLibro(@PathVariable String isbn, @Valid @RequestBody Libro libroToUpdate) {
        // Si no existe el libro a actualizar lanzará 404 NOT FOUND
        if (!libroRepository.existsById(isbn)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un libro con el ISBN " + isbn);
        }
        // Si el ISBN del path y el body no coincide, 400 BAD REQUEST
        if (!Objects.equals(libroToUpdate.getIsbn(), isbn)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: El ISBN del cuerpo de la solicitud no coincide con el ISBN de la URL.");
        }

        // Guardar el libro actualizado
        Libro updatedLibro = libroRepository.save(libroToUpdate);
        return ResponseEntity.ok(updatedLibro); // 200 OK + libro
    }

    // DELETE --> Eliminar un libro por su ISBN
    @DeleteMapping("/{isbn}")
    public ResponseEntity<?> deleteLibro(@PathVariable String isbn) {
        // Si el libro a eliminar no existe lanzará 404 NOT FOUND
        if (!libroRepository.existsById(isbn)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un libro con el ISBN " + isbn);
        }
        libroRepository.deleteById(isbn);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Eliminado correctamente"); // 204 NO CONTENT
    }
}

package g4vr3.bibliotecaapi.controller;

import g4vr3.bibliotecaapi.model.Ejemplar;
import g4vr3.bibliotecaapi.model.Libro;
import g4vr3.bibliotecaapi.model.Prestamo;
import g4vr3.bibliotecaapi.model.Usuario;
import g4vr3.bibliotecaapi.repository.EjemplarRepository;
import g4vr3.bibliotecaapi.repository.PrestamoRepository;
import g4vr3.bibliotecaapi.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public PrestamoController(PrestamoRepository prestamoRepository,
                              UsuarioRepository usuarioRepository,
                              EjemplarRepository ejemplarRepository) {
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
            return ResponseEntity.ok(prestamo); // 200 OK + prestamo
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un préstamo con el ID " + id); // 404 NOT FOUND
        }
    }

    //POST --> INSERT
    @PostMapping
    public ResponseEntity<?> postPrestamo(@Valid @RequestBody Prestamo prestamoToPost){
        // Si el préstamo a crear ya existe, 409 CONFLICT
        if (prestamoRepository.existsById(prestamoToPost.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: Ya existe un préstamo con el ID " + prestamoToPost.getId());
        }
        Prestamo postedPrestamo = prestamoRepository.save(prestamoToPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(postedPrestamo); // 201 CREATED
    }

    //POST con Form normal, se trabajará con JSONs normalmente...
//    @PostMapping(value = "/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Prestamo> postPrestamoForm(@RequestParam int id, @RequestParam int idEjemplar, @RequestParam int idUsuario,
//                                                     @RequestParam LocalDate fechaInicio, @RequestParam LocalDate fechaFin){
//        Prestamo prestamoToPost = new Prestamo();
//        prestamoToPost.setId(id);
//        // Obtener Usuario y Ejemplar a partir de los ID recibidos
//        Usuario usuario = this.usuarioRepository.findById(idUsuario).orElse(null); // Busca el Usuario por ID
//        Ejemplar ejemplar = this.ejemplarRepository.findById(idEjemplar).orElse(null); // Busca el Ejemplar por ID
//        prestamoToPost.setFechaInicio(fechaInicio);
//        prestamoToPost.setFechaDevolucion(fechaFin);
//        this.prestamoRepository.save(prestamoToPost);
//        return ResponseEntity.created(null).body(prestamoToPost);
//    }

    //PUT --> UPDATE
    //falta actualizar ficheros
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

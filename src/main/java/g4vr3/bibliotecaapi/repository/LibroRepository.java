package g4vr3.bibliotecaapi.repository;

import g4vr3.bibliotecaapi.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, String> {

}

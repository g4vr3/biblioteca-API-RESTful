package g4vr3.bibliotecaapi.repository;

import g4vr3.bibliotecaapi.model.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EjemplarRepository extends JpaRepository<Ejemplar, Integer> {
}

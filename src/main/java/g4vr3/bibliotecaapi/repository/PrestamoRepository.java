package g4vr3.bibliotecaapi.repository;

import g4vr3.bibliotecaapi.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
}

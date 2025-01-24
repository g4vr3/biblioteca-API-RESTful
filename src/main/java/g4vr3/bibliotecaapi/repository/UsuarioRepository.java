package g4vr3.bibliotecaapi.repository;

import g4vr3.bibliotecaapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

}

package g4vr3.bibliotecaapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
public class Libro {
    @Id
    @Size(max = 20)
    @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "Formato de ISBN inválido")
    @Column(name = "isbn", nullable = false, length = 20)
    private String isbn;

    @Size(max = 200)
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "El título debe contener solo caracteres alfanuméricos y espacios")
    @NotNull
    @NotEmpty
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "El autor debe contener solo caracteres alfanuméricos y espacios")
    @NotNull
    @NotEmpty
    @Column(name = "autor", nullable = false, length = 100)
    private String autor;

    @OneToMany(mappedBy = "libro")
    private Set<Ejemplar> ejemplares = new LinkedHashSet<>();
}
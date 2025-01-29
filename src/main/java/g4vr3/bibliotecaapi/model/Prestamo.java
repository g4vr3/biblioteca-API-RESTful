package g4vr3.bibliotecaapi.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @NotEmpty
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIncludeProperties({"id"})
    private Usuario usuario;

    @NotNull
    @NotEmpty
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ejemplar_id", nullable = false)
    @JsonIncludeProperties({"id"})
    private Ejemplar ejemplar;

    @NotNull
    @NotEmpty
    @Column(name = "fechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @NotEmpty
    @Column(name = "fechaDevolucion")
    private LocalDate fechaDevolucion;
}
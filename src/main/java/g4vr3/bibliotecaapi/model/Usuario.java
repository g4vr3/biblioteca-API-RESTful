package g4vr3.bibliotecaapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @NotEmpty
    @Column(name = "dni", nullable = false, length = 15)
    private String dni;

    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "El nombre debe contener solo caracteres alfanuméricos y espacios")
    @NotNull
    @NotEmpty
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 100)
    @Pattern(regexp = "[A-Za-z0-9]{1,50}@gmail\\.com", message = "El email debe ser un correo de Gmail")
    @NotNull
    @NotEmpty
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(min = 4, max = 12)
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "La contraseña debe ser una cadena alfanumérica de entre 4 y 12 caracteres")
    @NotNull
    @NotEmpty
    @Column(name = "password", nullable = false)
    private String password;

    @Pattern(regexp = "normal|administrador", message = "El tipo debe ser 'normal' o 'administrador'")
    @NotNull
    @NotEmpty
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "penalizacionHasta")
    private LocalDate penalizacionHasta;

    @OneToMany(mappedBy = "usuario")
    private Set<Prestamo> prestamos = new LinkedHashSet<>();

    public void setDni(String dni) {
        if (isDniValid(dni))
            this.dni = dni;
        else
            throw new IllegalArgumentException("El DNI introducido no es válido");
    }

    public static boolean isDniValid(String di) {
        // Verificar que el DI tiene 9 caracteres
        if (di.length() != 9) {
            return false;
        }
        return isControlDigitCorrect(di);
    }

    private static boolean isControlDigitCorrect(String dni) {
        List<Character> letras = List.of(
                'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B',
                'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'
        );

        // Obtener los caracteres numéricos del DNI
        int dniDigits = Integer.parseInt(dni.substring(0, 8));

        // Calcular el resto de la división del número por 23
        int resto = dniDigits % 23;

        // Obtener la letra introducida
        char letraIntroducida = dni.charAt(dni.length()-1);

        // Obtener la letra esperada de la lista
        char letraEsperada = letras.get(resto);

        // Comparar la letra introducida con la letra esperada
        return letraIntroducida == letraEsperada;
    }
}
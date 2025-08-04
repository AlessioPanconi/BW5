package buildweeek.bw2.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUtenteDTO(

        String username,
        @Email(message = "L'indirizzo email inserito non è nel formato giusto")
        String email,
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{4,}$", message = "La password deve contenere: 1 carat maiuscolo, uno minuscolo.....")
        @Size(min = 6, message = "password troppo corta")
        String password,
        String nome,
        String cognome,
        String ruolo,
        String passwordAdmin
){}

package buildweeek.bw2.DTO;

import buildweeek.bw2.enums.CustomerType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewClienteDTO(
        @NotEmpty(message = "L'indirizzo email è obbligatorio")
        @Email(message = "L'indirizzo email inserito non è nel formato giusto")
        String email,
        @NotEmpty(message = "La pec è obbligatoria")
        @Email(message = "L'indirizzo pec inserito non è nel formato giusto")
        String pec,
        @NotEmpty(message = "Il numero di telefono è obbligatorio!")
        String telefono,
        @NotEmpty(message = "La partita iva è obbligatoria!")
        @Size(min = 11, max = 11, message = "La partita iva ha 11 numeri!")
        String partitaIva,
        @NotEmpty(message = "La ragione sociale è obbligatoria!")
        String ragioneSociale,
        @NotEmpty(message = "La data di inserimento è obbligatoria!")
        String dataInserimento,
        @NotEmpty(message = "La data di ultimo contatto è obbligatoria!")
        String dataUltimoContatto,
        @NotEmpty(message = "Il fatturato annuale è obbligatorio!")
        double fatturatoAnnuale,
        @NotEmpty(message = "L'email del contatto è obbligatoria!")
        @Email(message = "L'email del contatto inserito non è nel formato giusto")
        String emailContatto,
        @NotEmpty(message = "Il provincia del contatto è obbligatorio!")
        @Size(min = 2, max = 30, message = "Il provincia deve essere compreso tra i 2 e i 30 caratteri!")
        String nomeContatto,
        @NotEmpty(message = "Il cognome del contatto è obbligatorio!")
        @Size(min = 2, max = 30, message = "Il cognome deve essere compreso tra i 2 e i 30 caratteri!")
        String cognomeContatto,
        @NotEmpty(message = "Il telefono del contatto è obbligatorio!")
        String telefonoContatto,
        @NotEmpty(message = "Il tipo di cliente è obbligatorio!")
        String customerType) {
}

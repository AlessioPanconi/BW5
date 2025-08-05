package buildweeek.bw2.DTO;

import buildweeek.bw2.entities.StatoFattura;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public record FatturaDTO(
        @NotEmpty(message = "La data della fattura è obbligatoria!")
        @FutureOrPresent(message = "La data non può non essere di un giorno passato!")
        LocalDate dataFattura,
        @NotEmpty(message ="La fattura deve avere un'importo!")
        double importo,
        @NotEmpty(message = "Il numero della fattura è obbligario!")
        long numero,
        @NotEmpty(message = "Lo stato della fattura è obbligatorio!")
        StatoFattura statoFattura
) {
}

package buildweeek.bw2.DTO;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record FatturaDTO(
        @NotEmpty(message = "La data della fattura è obbligatoria!")
        String dataFattura,
        @NotNull(message ="La fattura deve avere un'importo!")
        double importo,
        @NotNull(message = "Il numero della fattura è obbligatorio!")
        long numero
) {
}

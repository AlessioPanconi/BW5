package buildweeek.bw2.DTO.payloadMetodiClienti;

import jakarta.validation.constraints.NotNull;

public record FatturatoAnnualeDTO(@NotNull(message = "Il fatturato annuale è obbligatorio!")
                                  double fatturatoAnnuale){
}

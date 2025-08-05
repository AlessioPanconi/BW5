package buildweeek.bw2.DTO.payloadMetodiClienti;

import jakarta.validation.constraints.NotEmpty;

public record FatturatoAnnualeDTO(@NotEmpty(message = "Il fatturato annuale è obbligatorio!")
                                  double fatturatoAnnuale){
}

package buildweeek.bw2.DTO.payloadMetodiClienti;

import jakarta.validation.constraints.NotEmpty;

public record DataInserimentoDTO(

        @NotEmpty(message = "Data di inserimento necessaria")
        String dataInserimento
) {
}

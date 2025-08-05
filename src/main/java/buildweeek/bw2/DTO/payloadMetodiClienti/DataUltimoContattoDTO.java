package buildweeek.bw2.DTO.payloadMetodiClienti;

import jakarta.validation.constraints.NotEmpty;

public record DataUltimoContattoDTO(
        @NotEmpty(message = "La data di ultimo contatto è obbligatoria")
        String dataUltimoContatto
) {}

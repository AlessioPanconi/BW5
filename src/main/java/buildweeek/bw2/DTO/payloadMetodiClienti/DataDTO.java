package buildweeek.bw2.DTO.payloadMetodiClienti;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DataDTO(
        @NotEmpty(message = "La data non può essere vuota!")
        String dataFattura) {
}

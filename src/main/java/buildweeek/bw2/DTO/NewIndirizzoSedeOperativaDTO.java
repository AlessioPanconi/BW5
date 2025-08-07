package buildweeek.bw2.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewIndirizzoSedeOperativaDTO(

        @NotEmpty(message = "La via è obbligatoria")
        String via,
        @NotEmpty(message = "Il civico è obbligatorio")
        String civico,
        @NotEmpty(message = "La località è obbligatoria")
        String localita,
        @NotNull(message = "Il cap è obbligatorio")
        int cap,
        @NotEmpty(message = "Il nome del comune è obbligatorio")
        String comune

) {
}

package buildweeek.bw2.DTO.payloadMetodiClienti;

import jakarta.validation.constraints.NotEmpty;

public record PartialNameDTO(

        @NotEmpty(message = "Non può essere vuoto il parametro per cui fai la ricerca")
        String nomeContatto

) {}

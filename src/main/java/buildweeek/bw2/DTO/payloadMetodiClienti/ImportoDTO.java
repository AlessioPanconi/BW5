package buildweeek.bw2.DTO.payloadMetodiClienti;


import jakarta.validation.constraints.NotNull;

public record ImportoDTO(@NotNull
                         double importoMin,
                         @NotNull
                         double importoMax) {
}

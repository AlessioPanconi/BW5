package buildweeek.bw2.DTO.error;

import java.time.LocalDateTime;

public record ErrorsDTO(
        String message,
        LocalDateTime timestamp)
{}

package fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.errors;


import com.fasterxml.jackson.annotation.JsonFormat;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.ErrorDescriptor;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String code,
        String message,
        int status,
        String boundedContext,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime timestamp
) {
    public static ErrorResponseDto from(ErrorDescriptor descriptor) {
        return new ErrorResponseDto(
                descriptor.code(),
                descriptor.message(),
                descriptor.httpStatus(),
                descriptor.boundedContext(),
                LocalDateTime.now()
        );
    }
}

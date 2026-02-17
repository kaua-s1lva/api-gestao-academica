package br.ufes.ccens.api.dto.response;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record StudentResponse(
                @Schema(description = "Unique identifier of the student", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef") UUID studentId,

                @Schema(description = "Full name of the student", example = "João da Silva") String name,

                @Schema(description = "Email address of the student", example = "joao.silva@exemplo.com") String email,

                @Schema(description = "Registration number", example = "2023101234") String registration,

                @Schema(description = "Admission date", example = "01-03-2023") @JsonFormat(pattern = "dd-MM-yyyy") LocalDate admissionDate,

                @Schema(description = "Birth date", example = "15-05-2000") @JsonFormat(pattern = "dd-MM-yyyy") LocalDate birthDate) {
}

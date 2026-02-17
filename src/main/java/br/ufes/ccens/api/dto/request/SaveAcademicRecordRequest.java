package br.ufes.ccens.api.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record SaveAcademicRecordRequest(
                @NotNull(message = "O ID do estudante é obrigatório") @Schema(title = "Student ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID studentId,

                @NotNull(message = "O ID da disciplina é obrigatório") @Schema(title = "Discipline ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID disciplineId,

                @NotNull(message = "A frequência é obrigatória") @Min(value = 0) @Max(value = 100) @Schema(title = "Attendance", example = "85") Integer attendance,

                @NotNull(message = "A nota final é obrigatória") @PositiveOrZero @Max(value = 10) @Schema(title = "Final Grade", example = "9.5") Double finalGrade,

                @NotNull(message = "O semestre é obrigatório") @Schema(title = "Semester", example = "2023/1") String semester,

                @NotNull(message = "O status é obrigatório") @Schema(title = "Status", example = "Aprovado") String status,

                @Schema(title = "Observation", example = "Excelente aluno") String obs) {
}

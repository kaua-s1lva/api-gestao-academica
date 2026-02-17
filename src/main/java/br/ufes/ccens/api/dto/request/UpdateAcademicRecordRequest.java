package br.ufes.ccens.api.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record UpdateAcademicRecordRequest(
    @Schema(title = "Student ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") 
    UUID studentId,

    @Schema(title = "Discipline ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") 
    UUID disciplineId,

    @Min(value = 0) @Max(value = 100) 
    @Schema(title = "Attendance", example = "90") 
    Integer attendance,

    @PositiveOrZero 
    @Max(value = 10) 
    @Schema(title = "Final Grade", example = "8.5") 
    Double finalGrade,

    @Schema(title = "Semester", example = "2023/2") 
    String semester,

    @Schema(title = "Status", example = "Aprovado") 
    String status,

    @Schema(title = "Observation", example = "Melhorou seu desempenho") 
    String obs
) {}

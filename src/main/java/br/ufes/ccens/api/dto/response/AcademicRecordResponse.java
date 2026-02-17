package br.ufes.ccens.api.dto.response;

import java.util.UUID;

public record AcademicRecordResponse(
        UUID academicRecordId,
        StudentResponse student,
        DisciplineResponse discipline,
        Integer attendance,
        Double finalGrade,
        String semester,
        String status,
        String obs
) {}

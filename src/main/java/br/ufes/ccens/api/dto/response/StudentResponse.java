package br.ufes.ccens.api.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record StudentResponse (
    UUID studentId,
    String name,
    String email,
    String registration,
    LocalDate admissionDate,
    LocalDate birthDate
){}

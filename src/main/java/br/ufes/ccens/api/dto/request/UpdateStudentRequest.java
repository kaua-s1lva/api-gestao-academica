package br.ufes.ccens.api.dto.request;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;

public record UpdateStudentRequest(
    String name,

    @Email(message = "Formato de email inválido")
    String email,

    String registration,

    @Past(message = "A data de admissão deve estar no passado")
    LocalDate admissionDate,

    @Past(message = "A data de nascimento deve estar no passado")
    LocalDate birthDate,

    @CPF(message = "CPF inválido")
    String cpf
) {}

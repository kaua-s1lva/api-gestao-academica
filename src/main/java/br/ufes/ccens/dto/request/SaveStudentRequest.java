package br.ufes.ccens.dto.request;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record SaveStudentRequest (
    @NotBlank(message = "O nome é obrigatório")
    String name,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    String email,

    @NotBlank(message = "A senha é obrigatório")
    String password,

    @NotBlank(message = "O número de matrícula é obrigatório")
    String registration,

    @NotNull(message = "A data de admissão é obrigatório")
    @Past(message = "A data de admissão deve estar no passado")
    LocalDate admissionDate,

    @NotNull(message = "A data de nascimento é obrigatório")
    @Past(message = "A data de nascimento deve estar no passado")
    LocalDate birthDate,

    @NotBlank(message = "O CPF é obrigatório")
    @CPF(message = "CPF inválido")
    String cpf
) {
}

package br.ufes.ccens.api.dto.request;

import java.time.LocalDate;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record UpdateStudentRequest(
                @Schema(title = "Name", example = "João da Silva") String name,

                @Email(message = "Formato de email inválido") @Schema(title = "Email", example = "joao.silva@example.com") String email,

                @Pattern(regexp = "^\\d{10}$", message = "Formato de matrícula inválido. Deve conter 10 dígitos.") @Schema(title = "Registration", example = "2023100150") String registration,

                @Past(message = "A data de admissão deve estar no passado") @Schema(title = "Admission Date", example = "01-01-2022") @JsonFormat(pattern = "dd-MM-yyyy") LocalDate admissionDate,

                @Past(message = "A data de nascimento deve estar no passado") @Schema(title = "Birth Date", example = "01-01-2000") @JsonFormat(pattern = "dd-MM-yyyy") LocalDate birthDate,

                @CPF(message = "CPF inválido") @Schema(title = "CPF", example = "053.806.890-60") String cpf) {
}

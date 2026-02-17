package br.ufes.ccens.api.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public record SaveStudentRequest(
                @NotBlank(message = "O nome é obrigatório") @Schema(title = "Name", example = "João da Silva") String name,

                @NotBlank(message = "O email é obrigatório") @Email(message = "Formato de email inválido") @Schema(title = "Email", example = "joao.silva@example.com") String email,

                @NotBlank(message = "O número de matrícula é obrigatório") @Pattern(regexp = "^\\d{10}$", message = "Formato de matrícula inválido. Deve conter 10 dígitos.") @Schema(title = "Registration", example = "2023100150") String registration,

                @NotNull(message = "A data de admissão é obrigatório") @Past(message = "A data de admissão deve estar no passado") @Schema(title = "Admission Date", example = "01-01-2022") @JsonFormat(pattern = "dd-MM-yyyy") LocalDate admissionDate,

                @NotNull(message = "A data de nascimento é obrigatório") @Past(message = "A data de nascimento deve estar no passado") @Schema(title = "Birth Date", example = "01-01-2000") @JsonFormat(pattern = "dd-MM-yyyy") LocalDate birthDate,

                @NotBlank(message = "O CPF é obrigatório") @CPF(message = "CPF inválido") @Schema(title = "CPF", example = "053.806.890-60") String cpf) {
}

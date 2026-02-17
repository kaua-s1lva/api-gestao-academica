package br.ufes.ccens.api.dto.request;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import br.ufes.ccens.data.entity.enums.RoleUserEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record RegisterUserRequest(
        @NotBlank(message = "O nome é obrigatório") 
        @Schema(title = "Name", example = "Heldinho ReiDelus") 
        String name,

        @NotBlank(message = "O email é obrigatório") 
        @Email(message = "Formato de email inválido") 
        @Schema(title = "Email", example = "novo.usuario@email.com") 
        String email,

        @NotBlank(message = "A senha é obrigatório") 
        @Schema(title = "Password", example = "senha123") 
        String password,

        @NotNull(message = "O perfil (role) é obrigatório") 
        @Schema(title = "Role", example = "PROFESSOR") 
        RoleUserEnum role,

        @PositiveOrZero 
        @Schema(title = "Salary", example = "10000.0") 
        Double salary
) {}
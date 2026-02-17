package br.ufes.ccens.api.dto.request;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserRequest(
        @NotBlank(message = "O email é obrigatório") 
        @Email(message = "Formato de email inválido") 
        @Schema(title = "Email", example = "admin@email.com") 
        String email,

        @NotBlank(message = "A senha é obrigatório") 
        @Schema(title = "Password", example = "admin123") 
        String password
) {}

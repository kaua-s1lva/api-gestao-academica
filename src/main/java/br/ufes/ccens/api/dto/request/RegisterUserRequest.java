package br.ufes.ccens.api.dto.request;

import br.ufes.ccens.data.entity.enums.RoleUserEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record RegisterUserRequest(
    @NotBlank(message = "O nome é obrigatório")
    String name,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    String email,

    @NotBlank(message = "A senha é obrigatório")
    String password,

    @NotNull(message = "O perfil (role) é obrigatório")
    RoleUserEnum role,

    @PositiveOrZero
    Double salary
) {}
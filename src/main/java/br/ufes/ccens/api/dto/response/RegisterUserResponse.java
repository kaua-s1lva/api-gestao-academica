package br.ufes.ccens.api.dto.response;

import java.util.UUID;

import br.ufes.ccens.data.entity.enums.RoleUserEnum;

public record RegisterUserResponse (
    UUID userId,
    String name,
    String email,
    RoleUserEnum role
){}

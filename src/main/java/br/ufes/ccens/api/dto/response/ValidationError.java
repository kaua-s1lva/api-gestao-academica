package br.ufes.ccens.api.dto.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Detalhes do erro de validação")
public record ValidationError(
        @Schema(description = "Nome do campo com erro", example = "cpf") 
        String field,

        @Schema(description = "Motivo do erro", example = "Este CPF já está em uso.") 
        String reason
) {}

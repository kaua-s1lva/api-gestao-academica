package br.ufes.ccens.api.dto.response;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Resposta padrão de erro da API")
public record ApiErrorResponse(
        @Schema(description = "Código de status HTTP", example = "500") int status,

        @Schema(description = "Mensagem descritiva do erro", example = "Internal Server Error") String message,

        @Schema(description = "Lista de erros", example = "[]") List<String> errors) {
}

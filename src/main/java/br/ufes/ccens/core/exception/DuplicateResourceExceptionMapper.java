package br.ufes.ccens.core.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class DuplicateResourceExceptionMapper implements ExceptionMapper<DuplicateResourceException> {
    @Override
    public Response toResponse(DuplicateResourceException exception) {
        // Retorna status 409 (Conflict) e um JSON limpo e direto ao ponto
        return Response.status(Response.Status.CONFLICT)
                .entity(new ErrorMessage(exception.getMessage())) 
                .build();
    }
    
    // Record auxiliar apenas para formatar o JSON de resposta
    public record ErrorMessage(String message) {}
}

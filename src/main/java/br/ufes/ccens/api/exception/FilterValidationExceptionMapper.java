package br.ufes.ccens.api.exception;

import br.ufes.ccens.api.dto.response.ApiErrorResponse;
import br.ufes.ccens.core.exception.FilterValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class FilterValidationExceptionMapper implements ExceptionMapper<FilterValidationException> {

    @Override
    public Response toResponse(FilterValidationException exception) {
        var error = new ApiErrorResponse(
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Erro de validação nos filtros de busca.",
                exception.getErrors());

        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }
}

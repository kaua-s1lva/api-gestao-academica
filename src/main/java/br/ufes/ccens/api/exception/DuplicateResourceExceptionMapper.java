package br.ufes.ccens.api.exception;

import java.util.List;

import br.ufes.ccens.api.dto.response.ApiErrorResponse;
import br.ufes.ccens.api.dto.response.ValidationError;
import br.ufes.ccens.core.exception.DuplicateResourceException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DuplicateResourceExceptionMapper implements ExceptionMapper<DuplicateResourceException> {

    @Override
    public Response toResponse(DuplicateResourceException exception) {
        var validationError = new ValidationError(exception.getFieldName(), exception.getMessage());

        var apiError = new ApiErrorResponse(
                Response.Status.CONFLICT.getStatusCode(),
                "Conflito de dados.",
                List.of(validationError));

        return Response.status(Response.Status.CONFLICT).entity(apiError).build();
    }
}

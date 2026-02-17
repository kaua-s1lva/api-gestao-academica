package br.ufes.ccens.api.exception;

import br.ufes.ccens.core.exception.InvalidCredentialsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidCredentialsExceptionMapper implements ExceptionMapper<InvalidCredentialsException> {
    @Override
    public Response toResponse(InvalidCredentialsException exception) {
        var apiError = new br.ufes.ccens.api.dto.response.ApiErrorResponse(
                Response.Status.UNAUTHORIZED.getStatusCode(),
                exception.getMessage(),
                java.util.List.of());

        return Response.status(Response.Status.UNAUTHORIZED).entity(apiError).build();
    }
}

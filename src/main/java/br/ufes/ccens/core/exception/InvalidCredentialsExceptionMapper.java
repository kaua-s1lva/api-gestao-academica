package br.ufes.ccens.core.exception;

import br.ufes.ccens.api.dto.response.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidCredentialsExceptionMapper implements ExceptionMapper<InvalidCredentialsException> {
    @Override
    public Response toResponse(InvalidCredentialsException exception) {
        var error = new ErrorResponse(exception.getMessage(), 401);
        return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
    }
}

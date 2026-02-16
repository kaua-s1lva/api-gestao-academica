package br.ufes.ccens.api.exception;

import br.ufes.ccens.api.dto.response.ErrorResponse;
import br.ufes.ccens.core.exception.DuplicateResourceException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DuplicateResourceExceptionMapper implements ExceptionMapper<DuplicateResourceException> {
    @Override
    public Response toResponse(DuplicateResourceException exception) {
        var error = new ErrorResponse(exception.getMessage(), 409);
        return Response.status(Response.Status.CONFLICT).entity(error).build();
    }
}

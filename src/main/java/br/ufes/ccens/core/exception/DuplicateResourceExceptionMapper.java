package br.ufes.ccens.core.exception;

import br.ufes.ccens.api.dto.response.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class DuplicateResourceExceptionMapper implements ExceptionMapper<DuplicateResourceException> {
    @Override
    public Response toResponse(DuplicateResourceException exception) {
        var error = new ErrorResponse(exception.getMessage(), 409);
        return Response.status(Response.Status.CONFLICT).entity(error).build();
    }
}

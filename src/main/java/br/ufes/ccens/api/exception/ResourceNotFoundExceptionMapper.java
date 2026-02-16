package br.ufes.ccens.api.exception;

import br.ufes.ccens.api.dto.response.ErrorResponse;
import br.ufes.ccens.core.exception.ResourceNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {
    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        var error = new ErrorResponse(exception.getMessage(), 404);
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
}

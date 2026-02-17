package br.ufes.ccens.api.exception;

import java.util.List;

import br.ufes.ccens.api.dto.response.ApiErrorResponse;
import br.ufes.ccens.core.exception.ResourceNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        var apiError = new ApiErrorResponse(
                Response.Status.NOT_FOUND.getStatusCode(),
                exception.getMessage(),
                List.of());

        return Response.status(Response.Status.NOT_FOUND).entity(apiError).build();
    }
}

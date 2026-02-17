package br.ufes.ccens.api.exception;

import java.util.List;

import br.ufes.ccens.api.dto.response.ApiErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        exception.printStackTrace(); // Log the stack trace
        var error = new ApiErrorResponse(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Erro interno do servidor.",
                List.of());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }
}

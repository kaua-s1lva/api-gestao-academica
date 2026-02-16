package br.ufes.ccens.api.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException exception) {
        int status = exception.getResponse().getStatus();
        String message = exception.getMessage();

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "error", true,
                        "status", status,
                        "message", message != null ? message : "Erro desconhecido"))
                .build();
    }
}

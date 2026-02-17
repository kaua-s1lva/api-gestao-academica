package br.ufes.ccens.api.exception;

import java.util.stream.Collectors;

import br.ufes.ccens.api.dto.response.ApiErrorResponse;
import br.ufes.ccens.api.dto.response.ValidationError;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        var errors = exception.getConstraintViolations().stream()
                .map(violation -> {
                    String field = null;
                    for (var node : violation.getPropertyPath()) {
                        field = node.getName();
                    }
                    return new ValidationError(field, violation.getMessage());
                })
                .collect(Collectors.toList());

        var apiError = new ApiErrorResponse(
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Erro de validação de dados.",
                errors);

        return Response.status(Response.Status.BAD_REQUEST).entity(apiError).build();
    }
}

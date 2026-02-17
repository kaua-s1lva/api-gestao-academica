package br.ufes.ccens.api.exception;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.ufes.ccens.api.dto.response.ApiErrorResponse;
import br.ufes.ccens.api.dto.response.ValidationError;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JsonFormatExceptionMapper implements ExceptionMapper<InvalidFormatException> {

    @Override
    public Response toResponse(InvalidFormatException exception) {

        String errorMessage = "Valor inválido.";
        String fieldName = exception.getPath().isEmpty() ? "unknown"
                : exception.getPath().get(exception.getPath().size() - 1).getFieldName();

        if (exception.getTargetType() != null && exception.getTargetType().isEnum()) {
            String allowedValues = Arrays.stream(exception.getTargetType().getEnumConstants())
                    .map(e -> ((Enum<?>) e).name())
                    .collect(Collectors.joining(", "));
            errorMessage = "Valor inválido. Valores aceitos: " + allowedValues;
        }

        var validationError = new ValidationError(fieldName, errorMessage);

        var apiError = new ApiErrorResponse(
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Formato de dado inválido na requisição.",
                List.of(validationError));

        return Response.status(Response.Status.BAD_REQUEST).entity(apiError).build();
    }
}

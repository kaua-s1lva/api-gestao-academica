package br.ufes.ccens.api.exception;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.ufes.ccens.api.dto.response.ErrorResponse;
import br.ufes.ccens.data.entity.enums.RoleUserEnum;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JsonFormatExceptionMapper implements ExceptionMapper<InvalidFormatException> {
    @Override
    public Response toResponse(InvalidFormatException exception) {
        
        if (exception.getTargetType() != null && exception.getTargetType().isEnum()) {
            String roles = Arrays.stream(RoleUserEnum.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            var error = new ErrorResponse("Valor inválido. O perfil (role) não pode ser vazio ou inexistente. Valores aceitos: " + roles, 400);
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        var genericError = new ErrorResponse("Formato de dado inválido na requisição.", 400);
        return Response.status(Response.Status.BAD_REQUEST).entity(genericError).build();
    }
}

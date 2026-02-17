package br.ufes.ccens.api.exception;

import java.util.List;

import br.ufes.ccens.api.dto.response.ApiErrorResponse;
import br.ufes.ccens.core.exception.BusinessException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException exception) {
        var apiError = new ApiErrorResponse(
                Response.Status.BAD_REQUEST.getStatusCode(),
                exception.getMessage(),
                List.of());

        return Response.status(Response.Status.BAD_REQUEST).entity(apiError).build();
    }
}

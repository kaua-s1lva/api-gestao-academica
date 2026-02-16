package br.ufes.ccens.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AcademicRecordNotFoundExceptionMapper implements ExceptionMapper<AcademicRecordNotFoundException> {

    @Override
    public Response toResponse(AcademicRecordNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("REGISTRO ACADÊMICO NÃO ENCONTRADO!")
                       .build();
    }
}
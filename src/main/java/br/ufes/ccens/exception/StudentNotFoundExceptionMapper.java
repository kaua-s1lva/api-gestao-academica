package br.ufes.ccens.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class StudentNotFoundExceptionMapper implements ExceptionMapper<StudentNotFoundException> {
    @Override
    public Response toResponse(StudentNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Student Not Found").build();
    }
}

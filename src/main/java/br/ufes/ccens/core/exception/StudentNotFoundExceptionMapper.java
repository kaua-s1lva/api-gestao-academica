package br.ufes.ccens.core.exception;

import org.jboss.logging.Logger;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class StudentNotFoundExceptionMapper implements ExceptionMapper<StudentNotFoundException> {
    private static final Logger LOG = Logger.getLogger(StudentNotFoundExceptionMapper.class);
    
    @Override
    public Response toResponse(StudentNotFoundException exception) {
        LOG.warnf("Estudante não encontrado no banco de dados.");
        return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Student Not Found").build();
    }
}

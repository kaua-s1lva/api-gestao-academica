package br.ufes.ccens.controller;

import br.ufes.ccens.dto.request.LoginStudentRequest;
import br.ufes.ccens.entity.StudentEntity;
import br.ufes.ccens.service.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/login")
    public Response login(LoginStudentRequest loginRequest) {
        var tokenResponse = authService.login(loginRequest);
        return Response.ok(tokenResponse).build();
    }

    //VAMOS CRIAR UMA ROTA PARA QUALQUER USUÁRIO ACESSAR O SISTEMA?
    // @POST
    // @Path("/register")
    // @Transactional
    // public Response register(StudentEntity studentEntity) {
    //     var student = authService.register(studentEntity);
    //     return Response.status(Response.Status.CREATED).entity(student).build();
    // }
}

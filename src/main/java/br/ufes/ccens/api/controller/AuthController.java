package br.ufes.ccens.api.controller;

import br.ufes.ccens.api.dto.request.LoginUserRequest;
import br.ufes.ccens.api.dto.request.RegisterUserRequest;
import br.ufes.ccens.core.service.AuthService;
import br.ufes.ccens.data.entity.UserEntity;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid LoginUserRequest loginRequest) {
        var tokenResponse = authService.login(loginRequest);
        return Response.ok(tokenResponse).build();
    }

    @POST
    @Path("/register")
    @Transactional
    @RolesAllowed("ADMIN")
    public Response register(@Valid RegisterUserRequest studentEntity) {
        var student = authService.register(studentEntity);
        return Response.status(Response.Status.CREATED).entity(student).build();
    }
}

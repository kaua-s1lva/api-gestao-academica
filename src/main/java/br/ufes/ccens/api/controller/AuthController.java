package br.ufes.ccens.api.controller;

import org.jboss.logging.Logger;

import br.ufes.ccens.api.dto.request.LoginUserRequest;
import br.ufes.ccens.api.dto.request.RegisterUserRequest;
import br.ufes.ccens.core.service.AuthService;
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
    private static final Logger LOG = Logger.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid LoginUserRequest loginRequest) {
        LOG.infof("Tentativa de login para o email: %s", loginRequest.email());
        var tokenResponse = authService.login(loginRequest);
        LOG.info("Login realizado com sucesso!");
        return Response.ok(tokenResponse).build();
    }

    @POST
    @Path("/register")
    @Transactional
    @RolesAllowed("ADMIN")
    public Response register(@Valid RegisterUserRequest registerRequest) {
        LOG.infof("Tentativa de registro de novo usuario iniciada pelo ADMIN para o email: %s", registerRequest.email());
        var student = authService.register(registerRequest);
        LOG.info("Novo usuario registrado com sucesso no sistema.");
        return Response.status(Response.Status.CREATED).entity(student).build();
    }
}

package br.ufes.ccens.controller;

import br.ufes.ccens.request.LoginRequest;
import br.ufes.ccens.service.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PermitAll
public class AuthController {
    
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @POST
    public Response login(LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        return Response.ok().entity("{\"token\":\"" + token + "\"}").build();
    }
}

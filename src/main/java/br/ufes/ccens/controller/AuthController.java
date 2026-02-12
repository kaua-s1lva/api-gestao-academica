package br.ufes.ccens.controller;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;

import br.ufes.ccens.entity.StudentEntity;
import br.ufes.ccens.request.LoginRequest;
import br.ufes.ccens.service.AuthService;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

//@Path("/login")
public class AuthController {
    
    // private AuthService authService;

    // public AuthController(AuthService authService) {
    //     this.authService = authService;
    // }

    // @POST
    // @Path("/login")
    // @Transactional
    // public Response login(LoginRequest loginRequest) {
    //     return Response.ok(authService.login(loginRequest)).build();

    //     // 1. Buscar o estudante pelo email (Use Panache ou EntityManager)
    // }

}

package br.ufes.ccens.api.controller;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import br.ufes.ccens.api.docs.AuthExample;
import br.ufes.ccens.api.dto.request.LoginUserRequest;
import br.ufes.ccens.api.dto.request.RegisterUserRequest;
import br.ufes.ccens.api.dto.response.ApiErrorResponse;
import br.ufes.ccens.api.dto.response.RegisterUserResponse;
import br.ufes.ccens.api.dto.response.TokenResponse;
import br.ufes.ccens.core.service.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
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
    @APIResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class), examples = @ExampleObject(name = "Token", value = AuthExample.TOKEN_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Validation Error", value = AuthExample.BAD_REQUEST_RESPONSE)))
    @APIResponse(responseCode = "401", description = "Invalid Credentials", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Invalid Credentials", value = AuthExample.INVALID_CREDENTIALS_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = AuthExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response login(
            @Valid @RequestBody(description = "Login credentials", required = true, content = @Content(schema = @Schema(implementation = LoginUserRequest.class), examples = @ExampleObject(name = "Login Request", value = AuthExample.LOGIN_REQUEST))) LoginUserRequest loginRequest) {
        var tokenResponse = authService.login(loginRequest);
        return Response.ok(tokenResponse).build();
    }

    @POST
    @Path("/register")
    @RolesAllowed("ADMIN")
    @APIResponse(responseCode = "201", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterUserResponse.class), examples = @ExampleObject(name = "User Registered", value = AuthExample.REGISTER_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Validation Error", value = AuthExample.BAD_REQUEST_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = AuthExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response register(
            @Valid @RequestBody(description = "User registration data", required = true, content = @Content(schema = @Schema(implementation = RegisterUserRequest.class), examples = @ExampleObject(name = "Register Request", value = AuthExample.REGISTER_REQUEST))) RegisterUserRequest registerRequest) {
        var response = authService.register(registerRequest);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}

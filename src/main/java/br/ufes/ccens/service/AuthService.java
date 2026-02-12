package br.ufes.ccens.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;

import br.ufes.ccens.entity.StudentEntity;
import br.ufes.ccens.repository.StudentRepository;
import br.ufes.ccens.request.LoginRequest;
import br.ufes.ccens.util.GenerateToken;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

public class AuthService {
    // @Inject
    // @ConfigProperty(name = "mp.jwt.verify.issuer")
    // String issuer;
    
    private final GenerateToken generateToken;
    private final StudentRepository studentRepository;

    public AuthService(GenerateToken generateToken, StudentRepository studentRepository) {
        this.generateToken = generateToken;
        this.studentRepository = studentRepository;
    }

    public String login(LoginRequest loginRequest) {
        // StudentEntity student = studentRepository.find("email", loginRequest.email()).firstResult();

        // if (student != null && student.getPassword().equals(loginRequest.password())) {
            
        //     // 3. Gerar o Token JWT
        //     String token = Jwt.issuer(issuer)
        //                      .upn(student.getEmail())
        //                      .claim(Claims.full_name.name(), student.getName())
        //                      .sign();

        //     return Response.ok(new TokenResponse(token)).build();
        // }

        // return Response.status(Response.Status.UNAUTHORIZED).build(); // 401
        // //StudentEntity student = studentRepository.findByEmail
        return "teste";
    }
}

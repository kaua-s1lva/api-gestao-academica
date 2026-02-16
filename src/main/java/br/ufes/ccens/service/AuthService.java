package br.ufes.ccens.service;

// Leitura do token é feita em GenerateToken.java
//import org.eclipse.microprofile.config.inject.ConfigProperty;
// Geração do token feita em GenerateToken.java
//import org.eclipse.microprofile.jwt.Claims;

import br.ufes.ccens.entity.StudentEntity;
import br.ufes.ccens.repository.StudentRepository;
import br.ufes.ccens.request.LoginRequest;
import br.ufes.ccens.util.GenerateToken;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
// Geração do token feita em GenerateToken.java
//import io.smallrye.jwt.build.Jwt;
// Resposta HTTP é feita em Controller
//import jakarta.ws.rs.core.Response;

@ApplicationScoped
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
        // Busca estudante pelo email
        StudentEntity student = studentRepository.find("email", loginRequest.email()).firstResult();

        // RF09 – Autenticação
        if (student != null && student.getPassword() != null && student.getPassword().equals(loginRequest.password())) {
            
        //     // 3. Gerar o Token JWT
        //     String token = Jwt.issuer(issuer)
        //                      .upn(student.getEmail())
        //                      .claim(Claims.full_name.name(), student.getName())
        //                      .sign();

        //     return Response.ok(new TokenResponse(token)).build();
        // }

        // return Response.status(Response.Status.UNAUTHORIZED).build(); // 401
        // //StudentEntity student = studentRepository.findByEmail
        return generateToken.generateToken(student);
        }
        // Se não bater, retorna erro de não autorizado (401)
        throw new WebApplicationException("Email ou senha inválidos", 401);
    }
}

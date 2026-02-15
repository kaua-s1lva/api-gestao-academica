package br.ufes.ccens.core.service;

import br.ufes.ccens.api.dto.request.LoginStudentRequest;
import br.ufes.ccens.api.dto.response.TokenResponse;
import br.ufes.ccens.common.util.GenerateToken;
import br.ufes.ccens.data.entity.StudentEntity;
import br.ufes.ccens.data.repository.StudentRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class AuthService {

    private final GenerateToken generateToken;
    private final StudentRepository studentRepository;

    public AuthService(GenerateToken generateToken, StudentRepository studentRepository) {
        this.generateToken = generateToken;
        this.studentRepository = studentRepository;
    }

    public TokenResponse login(LoginStudentRequest loginRequest) {
        StudentEntity student = studentRepository.find("email", loginRequest.email()).firstResult();

        if (student == null) {
            throw new WebApplicationException("E-mail ou senha invalidos", Response.Status.UNAUTHORIZED);
        }

        if (!BcryptUtil.matches(loginRequest.password(), student.getPassword())) {
            throw new WebApplicationException("E-mail ou senha invalidos", Response.Status.UNAUTHORIZED);
        }

        String token = generateToken.generateToken(student);
        return new TokenResponse(token);
    }

    public StudentEntity register(StudentEntity studentEntity) {
        // Verificar se ja existe um estudante com esse email
        StudentEntity existing = studentRepository.find("email", studentEntity.getEmail()).firstResult();
        if (existing != null) {
            throw new WebApplicationException("E-mail ja cadastrado", Response.Status.CONFLICT);
        }

        // Hash da senha antes de persistir
        studentEntity.setPassword(BcryptUtil.bcryptHash(studentEntity.getPassword()));

        studentRepository.persist(studentEntity);
        return studentEntity;
    }
}

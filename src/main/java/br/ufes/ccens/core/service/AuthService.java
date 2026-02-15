package br.ufes.ccens.core.service;

import br.ufes.ccens.api.dto.request.LoginStudentRequest;
import br.ufes.ccens.api.dto.response.TokenResponse;
import br.ufes.ccens.common.util.GenerateToken;
import br.ufes.ccens.data.entity.StudentEntity;
import br.ufes.ccens.data.entity.UserEntity;
import br.ufes.ccens.data.repository.StudentRepository;
import br.ufes.ccens.data.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class AuthService {

    private final GenerateToken generateToken;
    private final UserRepository userRepository;

    public AuthService(GenerateToken generateToken, UserRepository userRepository) {
        this.generateToken = generateToken;
        this.userRepository = userRepository;
    }

    public TokenResponse login(LoginStudentRequest loginRequest) {
        UserEntity user = userRepository.find("email", loginRequest.email()).firstResult();

        if (user == null) {
            throw new WebApplicationException("E-mail ou senha invalidos", Response.Status.UNAUTHORIZED);
        }

        if (!BcryptUtil.matches(loginRequest.password(), user.getPassword())) {
            throw new WebApplicationException("E-mail ou senha invalidos", Response.Status.UNAUTHORIZED);
        }

        String token = generateToken.generateToken(user);
        return new TokenResponse(token);
    }

    public UserEntity register(UserEntity userEntity) {
        // Verificar se ja existe um estudante com esse email
        UserEntity existing = userRepository.find("email", userEntity.getEmail()).firstResult();
        if (existing != null) {
            throw new WebApplicationException("E-mail ja cadastrado", Response.Status.CONFLICT);
        }

        // Hash da senha antes de persistir
        userEntity.setPassword(BcryptUtil.bcryptHash(userEntity.getPassword()));

        userRepository.persist(userEntity);
        return userEntity;
    }
}

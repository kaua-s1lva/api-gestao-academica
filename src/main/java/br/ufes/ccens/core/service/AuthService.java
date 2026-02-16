package br.ufes.ccens.core.service;

import br.ufes.ccens.api.dto.request.LoginUserRequest;
import br.ufes.ccens.api.dto.request.RegisterUserRequest;
import br.ufes.ccens.api.dto.response.RegisterUserResponse;
import br.ufes.ccens.api.dto.response.TokenResponse;
import br.ufes.ccens.api.mapper.UserMapper;
import br.ufes.ccens.common.util.GenerateToken;
import br.ufes.ccens.core.exception.DuplicateResourceException;
import br.ufes.ccens.core.exception.InvalidCredentialsException;
import br.ufes.ccens.data.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AuthService {

    private final GenerateToken generateToken;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthService(GenerateToken generateToken, UserRepository userRepository, UserMapper userMapper) {
        this.generateToken = generateToken;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public TokenResponse login(LoginUserRequest loginRequest) {
        var user = userRepository.find("email", loginRequest.email()).firstResult();

        if (user == null || !BcryptUtil.matches(loginRequest.password(), user.getPassword())) {
            throw new InvalidCredentialsException("E-mail ou senha inválidos");
        }

        String token = generateToken.generateToken(user);
        return new TokenResponse(token);
    }

    @Transactional
    public RegisterUserResponse register(RegisterUserRequest registerRequest) {
        var userEntity = userMapper.toEntity(registerRequest);

        var existing = userRepository.find("email", userEntity.getEmail()).firstResult();
        if (existing != null) {
            throw new DuplicateResourceException("E-mail já cadastrado");
        }

        userEntity.setPassword(BcryptUtil.bcryptHash(userEntity.getPassword()));
        userRepository.persistAndFlush(userEntity);
        return userMapper.toResponse(userEntity);
    }
}

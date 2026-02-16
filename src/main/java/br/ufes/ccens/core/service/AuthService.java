package br.ufes.ccens.core.service;

import org.jboss.logging.Logger;

import br.ufes.ccens.api.dto.request.LoginUserRequest;
import br.ufes.ccens.api.dto.request.RegisterUserRequest;
import br.ufes.ccens.api.dto.response.TokenResponse;
import br.ufes.ccens.api.mapper.UserMapper;
import br.ufes.ccens.common.util.GenerateToken;
import br.ufes.ccens.data.entity.UserEntity;
import br.ufes.ccens.data.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class AuthService {

    private final GenerateToken generateToken;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final Logger LOG = Logger.getLogger(StudentService.class);

    public AuthService(GenerateToken generateToken, UserRepository userRepository, UserMapper userMapper) {
        this.generateToken = generateToken;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public TokenResponse login(LoginUserRequest loginRequest) {
        LOG.infof("Tentativa de login iniciada para o e-mail: %s", loginRequest.email());
        UserEntity user = userRepository.find("email", loginRequest.email()).firstResult();

        if (user == null) {
            LOG.warnf("Falha no login: Usuário com e-mail %s não encontrado.", loginRequest.email());
            throw new WebApplicationException("E-mail ou senha invalidos", Response.Status.UNAUTHORIZED);
        }

        if (!BcryptUtil.matches(loginRequest.password(), user.getPassword())) {
            LOG.warnf("Falha no login: Senha incorreta para o e-mail %s.", loginRequest.email());
            throw new WebApplicationException("E-mail ou senha invalidos", Response.Status.UNAUTHORIZED);
        }

        LOG.infof("Login bem-sucedido para o usuário: %s. Gerando token JWT...", user.getEmail());
        String token = generateToken.generateToken(user);
        return new TokenResponse(token);
    }

    public UserEntity register(RegisterUserRequest registerRequest) {
        LOG.infof("Solicitação de novo registro para o e-mail: %s", registerRequest.email());
        UserEntity userEntity = userMapper.toEntity(registerRequest); 

        UserEntity existing = userRepository.find("email", userEntity.getEmail()).firstResult();
        if (existing != null) {
            LOG.warnf("Falha no registro: O e-mail %s já está em uso.", userEntity.getEmail());
            throw new WebApplicationException("E-mail ja cadastrado", Response.Status.CONFLICT);
        }

        // Hash da senha antes de persistir
        userEntity.setPassword(BcryptUtil.bcryptHash(userEntity.getPassword()));

        userRepository.persist(userEntity);
        LOG.infof("Usuário %s registrado com sucesso no banco de dados.", userEntity.getEmail());
        return userEntity;
    }
}

package br.ufes.ccens.core.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ufes.ccens.api.dto.request.LoginUserRequest;
import br.ufes.ccens.api.dto.request.RegisterUserRequest;
import br.ufes.ccens.api.dto.response.RegisterUserResponse;
import br.ufes.ccens.api.mapper.UserMapper;
import br.ufes.ccens.common.util.GenerateToken;
import br.ufes.ccens.core.exception.InvalidCredentialsException;
import br.ufes.ccens.core.exception.DuplicateResourceException;
import br.ufes.ccens.core.service.AuthService;
import br.ufes.ccens.data.entity.UserEntity;
import br.ufes.ccens.data.repository.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    GenerateToken generateToken;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    AuthService authService;

    @Test
    void login_UsuarioNaoEncontrado_LancaInvalidCredentialsException() {
        // Given
        LoginUserRequest request = new LoginUserRequest("vini@ufes.br", "senha123");

        PanacheQuery<UserEntity> query = mock(PanacheQuery.class);    
        // Simulando que o banco não achou ninguém (firstResult retorna null)
        when(userRepository.find("email", "vini@ufes.br")).thenReturn(query);
        when(query.firstResult()).thenReturn(null);

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> {
            authService.login(request);
        });
        verify(generateToken, never()).generateToken(any());
    }

    @Test
    void register_EmailJaCadastrado_LancaDuplicateResourceException() {
        // Given
        RegisterUserRequest request = mock(RegisterUserRequest.class);
        UserEntity entity = new UserEntity();
        entity.setEmail("vini@ufes.br");
        
        when(userMapper.toEntity(request)).thenReturn(entity);
        
        PanacheQuery<UserEntity> query = mock(PanacheQuery.class);
        when(userRepository.find("email", "vini@ufes.br")).thenReturn(query);
        when(query.firstResult()).thenReturn(new UserEntity());

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> {
            authService.register(request);
        });
    }

    @Test
    void register_DadosValidos_DeveCadastrarComSucesso() {
        // Given
        RegisterUserRequest request = mock(RegisterUserRequest.class);
        UserEntity entity = new UserEntity();
        entity.setEmail("vini@ufes.br");
        entity.setPassword("senha123");
        
        RegisterUserResponse responseMock = mock(RegisterUserResponse.class);
        
        when(userMapper.toEntity(request)).thenReturn(entity);
        when(userMapper.toResponse(entity)).thenReturn(responseMock);
        
        // Simula que o e-mail está livre
        PanacheQuery<UserEntity> query = mock(PanacheQuery.class);
        when(userRepository.find("email", "vini@ufes.br")).thenReturn(query);
        when(query.firstResult()).thenReturn(null);

        // When
        var result = authService.register(request);

        // Then
        assertNotNull(result);
        verify(userRepository).persistAndFlush(entity);
    }
}

package br.ufes.ccens.core.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ufes.ccens.api.dto.request.SaveDisciplineRequest;
import br.ufes.ccens.api.dto.request.UpdateDisciplineRequest;
import br.ufes.ccens.api.dto.response.DisciplineResponse;
import br.ufes.ccens.api.mapper.DisciplineMapper;
import br.ufes.ccens.data.repository.DisciplineRepository;
import br.ufes.ccens.core.service.DisciplineService;
import br.ufes.ccens.core.validation.discipline.DisciplineFilterValidator;
import br.ufes.ccens.core.exception.DuplicateResourceException;
import br.ufes.ccens.core.exception.ResourceNotFoundException;
import br.ufes.ccens.data.entity.DisciplineEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

@ExtendWith(MockitoExtension.class)
public class DisciplineServiceTest {

    @Mock
    DisciplineRepository disciplineRepository;

    @Mock
    DisciplineMapper disciplineMapper;

    @Mock
    DisciplineFilterValidator disciplineFilterValidator;

    @InjectMocks
    DisciplineService disciplineService;
    
    @Test
    void createDiscipline_CodigoDuplicado_LancaDuplicateResourceException() {
        // Given
        SaveDisciplineRequest request = mock(SaveDisciplineRequest.class);
        when(request.cod()).thenReturn("DCC123");

        PanacheQuery<DisciplineEntity> query = mock(PanacheQuery.class);
        when(disciplineRepository.find("cod", "DCC123")).thenReturn(query);
        when(query.count()).thenReturn(1L); // Já existe uma disciplina com esse código

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> {
            disciplineService.createDiscipline(request);
        });
        
        verify(disciplineRepository, never()).persist(any(DisciplineEntity.class));
    }

    @Test
    void createDiscipline_DadosValidos_SalvaComSucesso() {
        // Given
        SaveDisciplineRequest request = mock(SaveDisciplineRequest.class);
        when(request.cod()).thenReturn("DCC123");
        
        DisciplineEntity entity = new DisciplineEntity();
        entity.setCod("DCC123");
        
        DisciplineResponse responseMock = mock(DisciplineResponse.class);

        PanacheQuery<DisciplineEntity> query = mock(PanacheQuery.class);
        when(disciplineRepository.find("cod", "DCC123")).thenReturn(query);
        when(query.count()).thenReturn(0L); // Código livre
        
        when(disciplineMapper.toEntity(request)).thenReturn(entity);
        when(disciplineMapper.toResponse(entity)).thenReturn(responseMock);

        // When
        DisciplineResponse result = disciplineService.createDiscipline(request);

        // Then
        assertNotNull(result);
        verify(disciplineRepository).persist(entity);
    }

    @Test
    void findById_DisciplinaExistente_RetornaDisciplineResponse() {
        // Given
        UUID id = UUID.randomUUID();
        DisciplineEntity entity = new DisciplineEntity();
        entity.setCod("DCC001");
        entity.setName("Estrutura de Dados");

        DisciplineResponse responseMock = mock(DisciplineResponse.class);

        when(disciplineRepository.findByIdOptional(id)).thenReturn(java.util.Optional.of(entity));
        when(disciplineMapper.toResponse(entity)).thenReturn(responseMock);

        // When
        DisciplineResponse result = disciplineService.findById(id);

        // Then
        assertNotNull(result);
        verify(disciplineRepository).findByIdOptional(id);
        verify(disciplineMapper).toResponse(entity);
    }

    @Test
    void findById_DisciplinaInexistente_LancaResourceNotFoundException() {
        // Given
        UUID id = UUID.randomUUID();
        when(disciplineRepository.findByIdOptional(id)).thenReturn(java.util.Optional.empty());

        // When e Then
        assertThrows(ResourceNotFoundException.class, () -> {
            disciplineService.findById(id);
        });

        verify(disciplineRepository).findByIdOptional(id);
        verify(disciplineMapper, never()).toResponse(any());
    }

    @Test
    void listAll_SemDisciplinas_LancaResourceNotFoundException() {
        // Given
        PanacheQuery<DisciplineEntity> mockQuery = mock(PanacheQuery.class);
        when(mockQuery.page(anyInt(), anyInt())).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(List.of()); // Lista vazia

        when(disciplineRepository.findWithFilters(any(), any(), any(), any(), any(), any())).thenReturn(mockQuery);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            disciplineService.listAll(0, 10, "name", "asc", "AlgumNome", null, null, null, null);
        });
    }

    @Test
    void updateDiscipline_DadosValidosSemMudarCodigo_AtualizaComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        UpdateDisciplineRequest request = mock(UpdateDisciplineRequest.class);
        when(request.cod()).thenReturn("DCC123"); // Mesmo código já existente

        DisciplineEntity entity = new DisciplineEntity();
        entity.setCod("DCC123");

        when(disciplineRepository.findByIdOptional(id)).thenReturn(Optional.of(entity));

        // When
        disciplineService.updateDiscipline(id, request);

        // Then
        verify(disciplineMapper).updateEntityFromDto(request, entity);
        verify(disciplineRepository).persist(entity);
    }

    @Test
    void updateDiscipline_MudaCodigoParaUmJaExistente_LancaDuplicateResourceException() {
        // Given
        UUID id = UUID.randomUUID();
        UpdateDisciplineRequest request = mock(UpdateDisciplineRequest.class);
        when(request.cod()).thenReturn("DCC456"); // Novo código que ele quer usar

        DisciplineEntity entity = new DisciplineEntity();
        entity.setCod("DCC123"); // Código atual da disciplina

        when(disciplineRepository.findByIdOptional(id)).thenReturn(Optional.of(entity));
        
        PanacheQuery<DisciplineEntity> query = mock(PanacheQuery.class);
        when(disciplineRepository.find("cod", "DCC456")).thenReturn(query);
        when(query.count()).thenReturn(1L);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> {
            disciplineService.updateDiscipline(id, request);
        });
        
        verify(disciplineRepository, never()).persist(any(DisciplineEntity.class));
        verify(disciplineMapper, never()).updateEntityFromDto(any(), any());
    }

    @Test
    void updateDiscipline_IdInexistente_LancaResourceNotFoundException() {
        // Given
        UUID id = UUID.randomUUID();
        when(disciplineRepository.findByIdOptional(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            disciplineService.updateDiscipline(id, mock(UpdateDisciplineRequest.class));
        });
    }

    @Test
    void updateDiscipline_DadosValidos_AtualizaComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        UpdateDisciplineRequest request = mock(UpdateDisciplineRequest.class);
        when(request.cod()).thenReturn("DCC123");

        DisciplineEntity entity = new DisciplineEntity();
        entity.setCod("DCC123");

        when(disciplineRepository.findByIdOptional(id)).thenReturn(Optional.of(entity));
        
        // When
        disciplineService.updateDiscipline(id, request);

        // Then
        verify(disciplineMapper).updateEntityFromDto(eq(request), eq(entity));
        verify(disciplineRepository).persist(any(DisciplineEntity.class));
    }

    @Test
    void deleteDiscipline_IdValido_DeletaComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        // Simulamos que o repositório conseguiu deletar (retorna true)
        when(disciplineRepository.deleteById(id)).thenReturn(true);

        // When
        disciplineService.deleteDiscipline(id);

        // Then
        verify(disciplineRepository).deleteById(id);
    }

    @Test
    void deleteDiscipline_IdInexistente_LancaResourceNotFoundException() {
        // Given
        UUID id = UUID.randomUUID();
        // Simulamos que o repositório não encontrou o ID para deletar (retorna false)
        when(disciplineRepository.deleteById(id)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            disciplineService.deleteDiscipline(id);
        });

        verify(disciplineRepository).deleteById(id);
    }
}

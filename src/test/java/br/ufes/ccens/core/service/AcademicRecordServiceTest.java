package br.ufes.ccens.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ufes.ccens.api.dto.request.SaveAcademicRecordRequest;
import br.ufes.ccens.api.dto.request.UpdateAcademicRecordRequest;
import br.ufes.ccens.api.dto.response.AcademicRecordResponse;
import br.ufes.ccens.api.mapper.AcademicRecordMapper;
import br.ufes.ccens.core.exception.ResourceNotFoundException;
import br.ufes.ccens.core.service.AcademicRecordService;
import br.ufes.ccens.core.validation.academicrecord.AcademicRecordFilterValidator;
import br.ufes.ccens.core.validation.academicrecord.AcademicRecordValidator;
import br.ufes.ccens.data.entity.AcademicRecordEntity;
import br.ufes.ccens.data.entity.DisciplineEntity;
import br.ufes.ccens.data.entity.StudentEntity;
import br.ufes.ccens.data.repository.AcademicRecordRepository;
import br.ufes.ccens.data.repository.DisciplineRepository;
import br.ufes.ccens.data.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
public class AcademicRecordServiceTest {

    @Mock AcademicRecordRepository academicRecordRepository;
    @Mock StudentRepository studentRepository;
    @Mock DisciplineRepository disciplineRepository;
    @Mock AcademicRecordMapper academicRecordMapper;
    @Mock AcademicRecordFilterValidator academicRecordFilterValidator;
    @Mock AcademicRecordValidator academicRecordValidator;

    @InjectMocks
    AcademicRecordService academicRecordService;

    @Test
    void createAcademicRecord_EstudanteNaoExiste_LancaResourceNotFoundException() {
        // Given
        UUID studentId = UUID.randomUUID();
        SaveAcademicRecordRequest request = mock(SaveAcademicRecordRequest.class);
        when(request.studentId()).thenReturn(studentId);
        
        when(academicRecordMapper.toEntity(request)).thenReturn(new AcademicRecordEntity());
        when(studentRepository.findByIdOptional(studentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            academicRecordService.createAcademicRecord(request);
        });
    }

    @Test
    void createAcademicRecord_DadosValidos_SalvaComSucesso() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID discId = UUID.randomUUID();
        SaveAcademicRecordRequest request = mock(SaveAcademicRecordRequest.class);
        when(request.studentId()).thenReturn(studentId);
        when(request.disciplineId()).thenReturn(discId);

        AcademicRecordEntity entity = new AcademicRecordEntity();
        AcademicRecordResponse responseMock = mock(AcademicRecordResponse.class);

        when(academicRecordMapper.toEntity(request)).thenReturn(entity);
        when(studentRepository.findByIdOptional(studentId)).thenReturn(Optional.of(new StudentEntity()));
        when(disciplineRepository.findByIdOptional(discId)).thenReturn(Optional.of(new DisciplineEntity()));
        when(academicRecordMapper.toResponse(entity)).thenReturn(responseMock);

        // When
        AcademicRecordResponse result = academicRecordService.createAcademicRecord(request);

        // Then
        assertNotNull(result);
        verify(academicRecordValidator).validate(entity);
        verify(academicRecordRepository).persist(entity);
    }

    @Test
    void listByDiscipline_DisciplinaNaoExiste_LancaResourceNotFoundException() {
        // Given
        UUID discId = UUID.randomUUID();
        when(disciplineRepository.findByIdOptional(discId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            academicRecordService.listByDiscipline(discId);
        });
    }

    @Test
    void listByStudent_EstudanteNaoExiste_LancaResourceNotFoundException() {
        // Given
        UUID studentId = UUID.randomUUID();
        when(studentRepository.findByIdOptional(studentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            academicRecordService.listByStudent(studentId, null);
        });
    }

    @Test
    void findById_RegistroExistente_RetornaResponse() {
        // Given
        UUID id = UUID.randomUUID();
        AcademicRecordEntity entity = new AcademicRecordEntity();
        AcademicRecordResponse responseMock = mock(AcademicRecordResponse.class);

        when(academicRecordRepository.findByIdOptional(id)).thenReturn(Optional.of(entity));
        when(academicRecordMapper.toResponse(entity)).thenReturn(responseMock);

        // When
        var result = academicRecordService.findById(id);

        // Then
        assertNotNull(result);
        verify(academicRecordRepository).findByIdOptional(id);
    }

    @Test
    void deleteAcademicRecord_IdInexistente_LancaResourceNotFoundException() {
        // Given
        UUID id = UUID.randomUUID();
        when(academicRecordRepository.deleteById(id)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            academicRecordService.deleteAcademicRecord(id);
        });
    }

    @Test
    void updateAcademicRecord_RegistroExiste_AtualizaComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        UUID newStudentId = UUID.randomUUID();
        UpdateAcademicRecordRequest request = mock(UpdateAcademicRecordRequest.class);
        when(request.studentId()).thenReturn(newStudentId);

        AcademicRecordEntity entityExistente = new AcademicRecordEntity();
        StudentEntity novoEstudante = new StudentEntity();

        when(academicRecordRepository.findByIdOptional(id)).thenReturn(Optional.of(entityExistente));
        when(studentRepository.findByIdOptional(newStudentId)).thenReturn(Optional.of(novoEstudante));

        // When
        academicRecordService.updateAcademicRecord(id, request);

        // Then
        verify(academicRecordMapper).updateEntityFromDto(request, entityExistente);
        verify(academicRecordValidator).validate(entityExistente);
        verify(academicRecordRepository).persist(entityExistente);
        assertEquals(novoEstudante, entityExistente.getStudent());
    }
}

package br.ufes.ccens.service;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ufes.ccens.api.dto.request.SaveStudentRequest;
import br.ufes.ccens.api.mapper.StudentMapper;
import br.ufes.ccens.core.service.StudentService;
import br.ufes.ccens.data.entity.StudentEntity;
import br.ufes.ccens.data.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    StudentRepository studentRepository;

    @Mock
    StudentMapper studentMapper;

    @InjectMocks
    StudentService studentService;

    @Test
    void CreateStudent_DadosValidos_PersistAndFlushEstudante() {
        @SuppressWarnings("null")
        SaveStudentRequest request = mock(SaveStudentRequest.class);
        StudentEntity entity = new StudentEntity();
        entity.setStudentId(UUID.randomUUID());

        when(studentMapper.toEntity(request)).thenReturn(entity);

        StudentEntity resultado = studentMapper.toEntity( studentService.createStudent(request) );

        assertNotNull(resultado);
        assertEquals(entity.getStudentId(), resultado.getStudentId());
        verify(studentRepository, times(1)).persistAndFlush(any(StudentEntity.class));
    }

    @Test
    void CreateStudent_ErroNoBancoDeDados_LancaRuntimeException() {
        @SuppressWarnings("null")
        SaveStudentRequest request = mock(SaveStudentRequest.class);
        when(studentMapper.toEntity(request)).thenReturn(new StudentEntity());
        
        doThrow(new RuntimeException("Erro de conexão")).when(studentRepository).persistAndFlush(any());

        assertThrows(RuntimeException.class, () -> {
            studentService.createStudent(request);
        });
    }

    @Test
    void ListAll_DataBrasileiraValida_ConverteParaLocalDate() {
        String dataBr = "15/02/2026";
        
        studentService.listAll(0, 10, null, null, null, null, dataBr, dataBr, null, null);

        verify(studentRepository).findWithFilters(
            any(), any(), any(), any(), 
            eq(LocalDate.of(2026, 2, 15)),
            eq(LocalDate.of(2026, 2, 15)), 
            any(), any(), anyInt(), anyInt()
        );
    }

    @Test
    void ListAll_FormatoDeDataInvalido_LancaRuntimeException() {
        @SuppressWarnings("null")
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.listAll(0, 10, null, null, null, null, "data-errada", null, null, null);
        });

        assertTrue(exception.getMessage().contains("Formato de data inválido"));
    }

    @Test
    void FindById_EstudanteInexistente_LancaStudentNotFoundException() {
        UUID idInexistente = UUID.randomUUID();
        
        when(studentRepository.findByIdOptional(idInexistente)).thenReturn(java.util.Optional.empty());

        assertThrows(br.ufes.ccens.core.exception.ResourceNotFoundException.class, () -> {
            studentService.findById(idInexistente);
        });
        
        verify(studentRepository).findByIdOptional(idInexistente);
    }
}
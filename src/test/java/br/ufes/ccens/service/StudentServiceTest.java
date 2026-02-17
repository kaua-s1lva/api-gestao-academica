package br.ufes.ccens.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
import br.ufes.ccens.api.dto.response.StudentResponse;
import br.ufes.ccens.api.mapper.StudentMapper;
import br.ufes.ccens.common.util.IConverterDataFormat;
import br.ufes.ccens.core.service.StudentService;
import br.ufes.ccens.core.validation.student.StudentValidator;
import br.ufes.ccens.data.entity.StudentEntity;
import br.ufes.ccens.data.repository.StudentRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    StudentRepository studentRepository;

    @Mock
    StudentMapper studentMapper;

    // 1. Adicionamos os Mocks das novas dependências da nossa arquitetura limpa!
    @Mock
    IConverterDataFormat converter;

    @Mock
    StudentValidator studentValidator;

    @InjectMocks
    StudentService studentService;

    @Test
    void CreateStudent_DadosValidos_PersistAndFlushEstudante() {
        SaveStudentRequest request = mock(SaveStudentRequest.class);
        StudentEntity entity = new StudentEntity();
        entity.setStudentId(UUID.randomUUID());

        // Mockamos a resposta também, pois o método agora retorna um StudentResponse
        StudentResponse responseMock = mock(StudentResponse.class);

        when(studentMapper.toEntity(request)).thenReturn(entity);
        when(studentMapper.toResponse(entity)).thenReturn(responseMock);

        StudentResponse resultado = studentService.createStudent(request);

        assertNotNull(resultado);
        // Verifica se a estratégia de validação foi chamada!
        verify(studentValidator, times(1)).validate(entity);
        verify(studentRepository, times(1)).persistAndFlush(entity);
    }

    @Test
    void CreateStudent_ErroNoBancoDeDados_LancaRuntimeException() {
        SaveStudentRequest request = mock(SaveStudentRequest.class);
        StudentEntity entity = new StudentEntity();

        when(studentMapper.toEntity(request)).thenReturn(entity);

        doThrow(new RuntimeException("Erro de conexão")).when(studentRepository).persistAndFlush(any());

        assertThrows(RuntimeException.class, () -> {
            studentService.createStudent(request);
        });

        // Garante que tentou validar antes de quebrar no banco
        verify(studentValidator, times(1)).validate(entity);
    }

    @Test
    @SuppressWarnings("unchecked")
    void ListAll_DataBrasileiraValida_BuscaComFiltros() {
        String dataBr = "15/02/2026";
        LocalDate dataConvertida = LocalDate.of(2026, 2, 15);

        // Como o Service não faz mais o parse, nós ensinamos o mock do conversor a
        // devolver a data correta
        when(converter.parse(dataBr)).thenReturn(dataConvertida);

        // Mock da Query do Panache para o service não dar NullPointerException se
        // chamar .page() ou .list()
        PanacheQuery<StudentEntity> mockQuery = mock(PanacheQuery.class);
        when(mockQuery.page(anyInt(), anyInt())).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(List.of()); // Retorna lista vazia

        when(studentRepository.findWithFilters(any(), any(), any(), any(), any(), any(), eq(dataConvertida),
                eq(dataConvertida), any()))
                .thenReturn(mockQuery);

        // Passamos "name" e "asc" pois o service não trata nulls neses campos (espera
        // defaults do controller)
        studentService.listAll(0, 10, "name", "asc", null, null, null, null, dataBr, dataBr, null, null);

        // Verifica a chamada correta ao repositório
        verify(studentRepository).findWithFilters(
                any(), any(), any(), any(),
                eq(dataConvertida), // admStart
                eq(dataConvertida), // admEnd
                any(), // birthStart
                any(), // birthEnd
                any() // sort
        );
    }

    @Test
    void ListAll_FormatoDeDataInvalido_LancaRuntimeException() {
        String dataErrada = "data-errada";

        // Simulamos o nosso Converter lançando a exceção
        when(converter.parse(dataErrada)).thenThrow(new RuntimeException("Formato de data inválido"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.listAll(0, 10, "name", "asc", null, null, dataErrada, null, null, null, null, null);
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
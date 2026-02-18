package br.ufes.ccens.core.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ufes.ccens.api.dto.request.SaveStudentRequest;
import br.ufes.ccens.api.dto.request.UpdateStudentRequest;
import br.ufes.ccens.api.dto.response.PageResponse;
import br.ufes.ccens.api.dto.response.StudentResponse;
import br.ufes.ccens.api.mapper.StudentMapper;
import br.ufes.ccens.common.util.IConverterDataFormat;
import br.ufes.ccens.core.exception.ResourceNotFoundException;
import br.ufes.ccens.core.service.StudentService;
import br.ufes.ccens.core.validation.student.StudentFilterValidator;
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

    @Mock
    StudentFilterValidator studentFilterValidator;

    @InjectMocks
    StudentService studentService;

    @Test
    @SuppressWarnings("null")
    void createStudent_DadosValidos_PersistAndFlushEstudante() {
        // Given
        SaveStudentRequest request = mock(SaveStudentRequest.class);
        StudentEntity entity = new StudentEntity();
        entity.setStudentId(UUID.randomUUID());
        entity.setName("Maik");
        entity.setEmail("maik@edu.ufes.br");
        entity.setCpf("297.116.210-96");
        entity.setAdmissionDate(LocalDate.now().minusDays(1));
        entity.setBirthDate(LocalDate.of(2011, 1, 1));

        // Mockamos a resposta também, pois o método agora retorna um StudentResponse
        StudentResponse responseMock = mock(StudentResponse.class);

        when(studentMapper.toEntity(request)).thenReturn(entity);
        when(studentMapper.toResponse(entity)).thenReturn(responseMock);

        // When
        StudentResponse resultado = studentService.createStudent(request);
 
        // Then
        assertNotNull(resultado);
        // Verifica se a estratégia de validação foi chamada!
        verify(studentValidator, times(1)).validate(entity);
        verify(studentRepository, times(1)).persistAndFlush(entity);
    }

    @Test
    @SuppressWarnings("null")
    void createStudent_ErroNoBancoDeDados_LancaRuntimeException() {
        // Given
        SaveStudentRequest request = mock(SaveStudentRequest.class);
        StudentEntity entity = new StudentEntity();
        entity.setStudentId(UUID.randomUUID());
        entity.setName("Vinicius");
        entity.setEmail("vinicius@edu.ufes.br");
        entity.setCpf("256.805.780-77");
        entity.setAdmissionDate(LocalDate.now().minusDays(1));
        entity.setBirthDate(LocalDate.of(2008, 1, 1));

        when(studentMapper.toEntity(request)).thenReturn(entity);

        doThrow(new RuntimeException("Erro de conexão")).when(studentRepository).persistAndFlush(any());

        // When e Then
        assertThrows(RuntimeException.class, () -> {
            studentService.createStudent(request);
        });

        // Garante que tentou validar antes de quebrar no banco
        verify(studentValidator, times(1)).validate(entity);
    }

    @Test
    @SuppressWarnings({"unchecked", "null"})
    void listAll_DataBrasileiraValida_BuscaComFiltros() {
        // Given
        String dataBr = "15/02/2026";
        LocalDate dataConvertida = LocalDate.of(2026, 2, 15);

        StudentEntity fakeEntity = new StudentEntity();
        StudentResponse responseMock = mock(StudentResponse.class);
        // Como o Service não faz mais o parse, nós ensinamos o mock do conversor a
        // devolver a data correta
        when(converter.parse(dataBr)).thenReturn(dataConvertida);

        // Mock da Query do Panache para o service não dar NullPointerException se
        // chamar .page() ou .list()
        PanacheQuery<StudentEntity> mockQuery = mock(PanacheQuery.class);
        when(mockQuery.page(anyInt(), anyInt())).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(List.of(fakeEntity)); // Retorna lista vazia
        when(mockQuery.count()).thenReturn(1L); // Para o PageResponse não dar erro
        when(mockQuery.pageCount()).thenReturn(1);
        when(studentRepository.findWithFilters(any(), any(), any(), any(), eq(dataConvertida), eq(dataConvertida), eq(dataConvertida),
                eq(dataConvertida), any()))
                .thenReturn(mockQuery);
        when(studentMapper.toResponse(fakeEntity)).thenReturn(responseMock);

        // When
        // Passamos "name" e "asc" pois o service não trata nulls neses campos (espera
        // defaults do controller)
        PageResponse<StudentResponse> resultado = 
            studentService.listAll(0, 10, "name", "asc", null, null, null, null, dataBr, dataBr, dataBr, dataBr);

        // Then (Verificação)
        assertNotNull(resultado); // Garante que o serviço encapsulou a lista no PageResponse
        verify(studentRepository).findWithFilters(
                any(), any(), any(), any(),
                eq(dataConvertida), 
                eq(dataConvertida), 
                eq(dataConvertida), 
                eq(dataConvertida), 
                any()
        );
    }

    @Test
    @SuppressWarnings("null")
    void listAll_FormatoDeDataInvalido_LancaRuntimeException() {
        // Given
        String dataErrada = "data-errada";
        // Simulamos o nosso Converter lançando a exceção
        when(converter.parse(dataErrada)).thenThrow(new RuntimeException("Formato de data inválido"));

        // When & Then
        // Warning alertando possibilidade de valor NULL, na prática se o teste passar o valor nunca será NULL
        // Portanto podemos ignora-lo
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                studentService.listAll(0, 10, "name", "asc", null, null, dataErrada, null, null, null, null, null);
            });

        assertNotNull(exception.getMessage());
    }

    @Test
    void listAll_SemResultados_LancaResourceNotFoundException() {
        PanacheQuery<StudentEntity> mockQuery = mock(PanacheQuery.class);
        when(mockQuery.page(anyInt(), anyInt())).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(List.of());
        when(studentRepository.findWithFilters(
            any(), any(), any(), any(), any(), 
            any(), any(), any(), any()))
            .thenReturn(mockQuery);
        
        assertThrows(ResourceNotFoundException.class, () -> {
            studentService.listAll(0, 10, "name", "asc", "Nome", null, null, null, null, null, null, null);
        });
    }

    @Test
    void findById_EstudanteInexistente_LancaResourceNotFoundException() {
        // Given
        UUID idInexistente = UUID.randomUUID();
        when(studentRepository.findByIdOptional(idInexistente)).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
                studentService.findById(idInexistente);
            });

        verify(studentRepository).findByIdOptional(idInexistente);
    }

    @Test
    void findById_EstudanteExiste_RetornaResponse() {
        UUID id = UUID.randomUUID();
        StudentEntity entity = new StudentEntity(); // preencha
        StudentResponse responseMock = mock(StudentResponse.class);
        
        when(studentRepository.findByIdOptional(id)).thenReturn(Optional.of(entity));
        when(studentMapper.toResponse(entity)).thenReturn(responseMock);
        
        StudentResponse result = studentService.findById(id);
        assertNotNull(result);
        verify(studentMapper).toResponse(entity);
    }

    @Test
    @SuppressWarnings("null")
    void createStudent_CpfNulo_DeveLancarExcecaoDeValidacao() {
        // Given
        SaveStudentRequest request = mock(SaveStudentRequest.class);
        StudentEntity entity = new StudentEntity();
        entity.setStudentId(UUID.randomUUID());
        entity.setName("Kaua");
        entity.setEmail("kaua@edu.ufes.br");
        entity.setCpf("842.286.840-76");
        entity.setAdmissionDate(LocalDate.now().minusDays(1));
        entity.setBirthDate(LocalDate.of(2011, 1, 1));

        when(studentMapper.toEntity(request)).thenReturn(entity);
        doThrow(jakarta.validation.ConstraintViolationException.class)
            .when(studentValidator).validate(entity);

        // When e Then
        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            studentService.createStudent(request);
        });
    }

    @Test
    void updateStudent_EstudanteExiste_AtualizaComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        UpdateStudentRequest request = mock(UpdateStudentRequest.class);
        
        StudentEntity entityExistente = new StudentEntity();
        entityExistente.setStudentId(id);
        entityExistente.setName("Antigo Nome");

        StudentEntity novaEntity = new StudentEntity();
        novaEntity.setName("Novo Nome");

        when(studentRepository.findByIdOptional(id)).thenReturn(java.util.Optional.of(entityExistente));
        when(studentMapper.toEntity(request)).thenReturn(novaEntity);

        // When
        studentService.updateStudent(id, request);

        // Then
        verify(studentValidator).validate(any());
        verify(studentMapper).updateEntityFromDto(eq(request), eq(entityExistente));
        verify(studentRepository).persist(entityExistente);
    }

    @Test
    void deleteStudent_EstudanteExiste_DeletaComSucesso() {
        // Given
        UUID id = UUID.randomUUID();
        StudentEntity entity = new StudentEntity();
        entity.setStudentId(id);
        when(studentRepository.findByIdOptional(id)).thenReturn(java.util.Optional.of(entity));

        // When
        studentService.deleteStudent(id);

        // Then
        verify(studentRepository).deleteById(id);
    }

    @Test
    void deleteStudent_NaoExiste_LancaResourceNotFoundException() {
        UUID id = UUID.randomUUID();
        when(studentRepository.findByIdOptional(id)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(id));
    }
}
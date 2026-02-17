package br.ufes.ccens.core.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import br.ufes.ccens.api.dto.request.SaveStudentRequest;
import br.ufes.ccens.api.dto.request.UpdateStudentRequest;
import br.ufes.ccens.api.dto.response.PageResponse;
import br.ufes.ccens.api.dto.response.StudentResponse;
import br.ufes.ccens.api.mapper.StudentMapper;
import br.ufes.ccens.common.util.IConverterDataFormat;
import br.ufes.ccens.core.exception.ResourceNotFoundException;
import br.ufes.ccens.core.interceptor.LogTransaction;
import br.ufes.ccens.core.validation.student.StudentValidator;
import br.ufes.ccens.data.entity.StudentEntity;
import br.ufes.ccens.data.repository.StudentRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@LogTransaction
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final IConverterDataFormat converter;
    private final StudentValidator studentValidator;

    public StudentService(
            StudentRepository studentRepository, 
            StudentMapper studentMapper, 
            IConverterDataFormat converter,
            StudentValidator studentValidator) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.converter = converter;
        this.studentValidator = studentValidator;
    }

    @Transactional
    public StudentResponse createStudent(SaveStudentRequest studentRequest) {
        var studentEntity = studentMapper.toEntity(studentRequest);
        studentValidator.validate(studentEntity);
        studentRepository.persistAndFlush(studentEntity);
        return studentMapper.toResponse(studentEntity);
    }

    public PageResponse<StudentResponse> listAll(Integer page, Integer pageSize, String sortBy, String sortDir, String name, String email, 
                                                    String registration, String cpf, String admStart, 
                                                    String admEnd, String birthStart, String birthEnd) {
                                                    
        LocalDate admissionStart = converter.parse(admStart);
        LocalDate admissionEnd = converter.parse(admEnd);
        LocalDate birthStartDate = converter.parse(birthStart);
        LocalDate birthEndDate = converter.parse(birthEnd);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.Descending : Sort.Direction.Ascending;
        Sort sort = Sort.by(sortBy, direction);

        PanacheQuery<StudentEntity> query;

        if (name != null || email != null || registration != null || cpf != null || admissionStart != null || birthStartDate != null) {
            query = studentRepository.findWithFilters(name, email, registration, cpf, admissionStart, 
                        admissionEnd, birthStartDate, birthEndDate, sort);
        } else {
            query = studentRepository.findAll(sort);
        }

        query.page(page, pageSize);

        List<StudentResponse> content = query.list().stream()
                .map(studentMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page,
                pageSize,
                query.count(),
                query.pageCount()
        );
    }

    public StudentResponse findById(UUID studentId) {
        var student = getStudentEntity(studentId);
        return studentMapper.toResponse(student);
    }

    @Transactional
    public StudentResponse updateStudent(UUID studentId, UpdateStudentRequest studentRequest) {
        var studentEntity = getStudentEntity(studentId);
        var newStudent = studentMapper.toEntity(studentRequest);
        newStudent.setStudentId(studentId);

        studentValidator.validate(newStudent);
        studentMapper.updateEntityFromDto(studentRequest, studentEntity);
        studentRepository.persist(studentEntity);

        return studentMapper.toResponse(studentEntity);
    }

    @Transactional
    public void deleteStudent(UUID studentId) {
        var student = getStudentEntity(studentId);
        studentRepository.deleteById(student.getStudentId());
    }

    private StudentEntity getStudentEntity(UUID studentId) {
        return studentRepository.findByIdOptional(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Estudante não encontrado com o ID fornecido."));
    }
}

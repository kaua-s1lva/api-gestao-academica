package br.ufes.ccens.core.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;

import br.ufes.ccens.api.dto.request.SaveStudentRequest;
import br.ufes.ccens.api.dto.request.UpdateStudentRequest;
import br.ufes.ccens.api.dto.response.StudentResponse;
import br.ufes.ccens.api.mapper.StudentMapper;
import br.ufes.ccens.common.util.IConverterDataFormat;
import br.ufes.ccens.core.exception.DuplicateResourceException;
import br.ufes.ccens.core.exception.ResourceNotFoundException;
import br.ufes.ccens.core.validation.student.StudentValidator;
import br.ufes.ccens.data.entity.StudentEntity;
import br.ufes.ccens.data.repository.StudentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final IConverterDataFormat converter;
    private final StudentValidator studentValidator;
    private static final Logger LOG = Logger.getLogger(StudentService.class);

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
/*
        var existingEmail = studentRepository.find("email", studentEntity.getEmail()).firstResult();
        if (existingEmail != null) {
            LOG.error("Email já cadastrado");
            throw new DuplicateResourceException("Email já cadastrado");
        }

        var existingCpf = studentRepository.find("cpf", studentEntity.getCpf()).firstResult();

        if (existingCpf != null) {
            LOG.error("Cpf já cadastrado");
            throw new DuplicateResourceException("Cpf já cadastrado");
        }
 */
        studentValidator.validate(studentEntity);
        studentRepository.persistAndFlush(studentEntity);
        LOG.info("Estudante criado com sucesso: " + studentEntity.getStudentId());
        return studentMapper.toResponse(studentEntity);
    }
/* 
    private LocalDate parseDate(String dateStr) {
        
        
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }

        try {
            // Tenta o padrão ISO (2026-02-15) - Padrão internacional
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            try {
                // Se falhar, tenta o padrão brasileiro (15/02/2026)
                var brFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return LocalDate.parse(dateStr, brFormatter);
            } catch (Exception e2) {
                // Se falhar nos dois, avisa o erro
                LOG.error("Erro ao converter data: " + dateStr);
                throw new RuntimeException("Formato de data inválido [" + dateStr + "]. Use AAAA-MM-DD ou DD/MM/AAAA");
            }
        }
            
        if (dateStr == null || dateStr.isBlank()) return null;
        return LocalDate.parse(dateStr, MULTI_FORMATTER);
    }
*/
    public List<StudentResponse> listAll(Integer page, Integer pageSize, String name, String email, String registration, String cpf,
                                  String admStart, String admEnd, String birthStart, String birthEnd) {
        LocalDate admissionStart = converter.parse(admStart);
        LocalDate admissionEnd = converter.parse(admEnd);
        LocalDate birthStartDate = converter.parse(birthStart);
        LocalDate birthEndDate = converter.parse(birthEnd);

        List<StudentEntity> students;

        if (name != null || email != null || registration != null || cpf != null || admissionStart != null || birthStartDate != null) {
            LOG.info("Realizando busca com filtros inseridos");
            students = studentRepository.findWithFilters(name, email, registration, cpf, admissionStart, 
                        admissionEnd, birthStartDate, birthEndDate, page, pageSize);
        } else {
            LOG.info("Listando todos os estudantes (sem filtros)");
            students = studentRepository.findAll().page(page, pageSize).list();
        }

        return students.stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    public StudentResponse findById(UUID studentId) {
        LOG.info("Buscando estudante pelo ID: " + studentId);
        var student = getStudentEntity(studentId);
        return studentMapper.toResponse(student);
    }

    @Transactional
    public StudentResponse updateStudent(UUID studentId, UpdateStudentRequest studentRequest) {
        LOG.info("Atualizando dados do estudante ID: " + studentId);
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
        LOG.info("Solicitação de exclusão para o estudante ID: " + studentId);
        var student = getStudentEntity(studentId);
        studentRepository.deleteById(student.getStudentId());
    }

    private StudentEntity getStudentEntity(UUID studentId) {
        return studentRepository.findByIdOptional(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Estudante não encontrado com o ID fornecido."));
    }
}

package br.ufes.ccens.core.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;

import br.ufes.ccens.api.dto.request.SaveStudentRequest;
import br.ufes.ccens.api.dto.response.StudentResponse;
import br.ufes.ccens.api.mapper.StudentMapper;
import br.ufes.ccens.core.exception.DuplicateResourceException;
import br.ufes.ccens.core.exception.StudentNotFoundException;
import br.ufes.ccens.data.entity.StudentEntity;
import br.ufes.ccens.data.repository.StudentRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private static final Logger LOG = Logger.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public StudentResponse createStudent(SaveStudentRequest studentRequest) {
        var studentEntity = studentMapper.toEntity(studentRequest);
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

        studentRepository.persistAndFlush(studentEntity);
        LOG.info("Estudante criado com sucesso: " + studentEntity.getStudentId());
        return studentMapper.toResponse(studentEntity);
    }

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
    }

    public List<StudentResponse> listAll(Integer page, Integer pageSize, String name, String email, String registration, String cpf,
                                  String admStart, String admEnd, String birthStart, String birthEnd) {
        LocalDate admissionStart = parseDate(admStart);
        LocalDate admissionEnd = parseDate(admEnd);
        LocalDate birthStartDate = parseDate(birthStart);
        LocalDate birthEndDate = parseDate(birthEnd);

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
        var student = studentRepository.findByIdOptional(studentId)
            .orElseThrow(StudentNotFoundException::new);
        return studentMapper.toResponse(student);
    }

    public StudentResponse updateStudent(UUID studentId, SaveStudentRequest studentRequest) {
        LOG.info("Atualizando dados do estudante ID: " + studentId);
        var studentEntity = studentRepository.findByIdOptional(studentId)
            .orElseThrow(StudentNotFoundException::new);
        
        var newStudent = studentMapper.toEntity(studentRequest);

        if (newStudent.getName() != null && !newStudent.getName().isBlank()) {
            studentEntity.setName(newStudent.getName());
        }
        if (newStudent.getRegistration() != null && !newStudent.getRegistration().isBlank()) {
            studentEntity.setRegistration(newStudent.getRegistration());
        }
        if (newStudent.getCpf() != null && !newStudent.getCpf().isBlank()) {
            studentEntity.setCpf(newStudent.getCpf());
        }
        if (newStudent.getAdmissionDate() != null) {
            studentEntity.setAdmissionDate(newStudent.getAdmissionDate());
        }
        if (newStudent.getBirthDate() != null) {
            studentEntity.setBirthDate(newStudent.getBirthDate());
        }

        studentRepository.persist(studentEntity);

        return studentMapper.toResponse(studentEntity);
    }

    public void deleteStudent(UUID studentId) {
        LOG.info("Solicitação de exclusão para o estudante ID: " + studentId);
        var student = studentRepository.findByIdOptional(studentId)
            .orElseThrow(StudentNotFoundException::new);
        studentRepository.deleteById(student.getStudentId());
    }
}

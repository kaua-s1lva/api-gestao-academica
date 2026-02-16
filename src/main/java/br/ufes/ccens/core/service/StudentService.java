package br.ufes.ccens.core.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;

import org.jboss.logging.Logger;

import br.ufes.ccens.api.dto.request.SaveStudentRequest;
import br.ufes.ccens.api.mapper.StudentMapper;
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

    public StudentEntity createStudent(SaveStudentRequest studentRequest) {
        try {
            var studentEntity = studentMapper.toEntity(studentRequest);
            studentRepository.persistAndFlush(studentEntity);
            LOG.info("Estudante criado com sucesso: " + studentEntity.getStudentId());
            return studentEntity;
        } catch (Exception e) {
            LOG.error("Erro ao criar estudante: " + e.getMessage());
            throw new RuntimeException("Erro ao criar usuário: " + e.getMessage());
        }
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

    public List<StudentEntity> listAll(Integer page, Integer pageSize, String name, String email, String registration, String cpf,
                                  String admStart, String admEnd, String birthStart, String birthEnd) {
        LocalDate admissionStart = parseDate(admStart);
        LocalDate admissionEnd = parseDate(admEnd);
        LocalDate birthStartDate = parseDate(birthStart);
        LocalDate birthEndDate = parseDate(birthEnd);

        if (name != null || email != null || registration != null || cpf != null || admissionStart != null || birthStartDate != null) {
        
            LOG.info("Realizando busca com filtros inseridos");
            return studentRepository.findWithFilters(name, email, registration, cpf, admissionStart, 
                        admissionEnd, birthStartDate, birthEndDate, page, pageSize);
        }

        LOG.info("Listando todos os estudantes (sem filtros)");
        return studentRepository.findAll().page(page, pageSize).list();
    }

    public StudentEntity findById(UUID studentId) {
        LOG.info("Buscando estudante pelo ID: " + studentId);
        return studentRepository.findByIdOptional(studentId)
            .orElseThrow(StudentNotFoundException::new);
    }

    public StudentEntity updateStudent(UUID studentId, SaveStudentRequest studentRequest) {
        LOG.info("Atualizando dados do estudante ID: " + studentId);
        var student = findById(studentId);
        
        var studentEntity = studentMapper.toEntity(studentRequest);

        student.setName(studentEntity.getName());
        student.setRegistration(studentEntity.getRegistration());
        student.setAdmissionDate(studentEntity.getAdmissionDate());
        student.setBirthDate(studentEntity.getBirthDate());
        student.setCpf(studentEntity.getCpf());

        studentRepository.persist(student);

        return student;
    }

    public void deleteStudent(UUID studentId) {
        LOG.info("Solicitação de exclusão para o estudante ID: " + studentId);
        var student = findById(studentId);
        studentRepository.deleteById(student.getStudentId());
    }
}

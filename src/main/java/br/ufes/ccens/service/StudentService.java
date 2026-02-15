package br.ufes.ccens.service;

import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;

import br.ufes.ccens.dto.request.SaveStudentRequest;
import br.ufes.ccens.entity.StudentEntity;
import br.ufes.ccens.exception.StudentNotFoundException;
import br.ufes.ccens.mapper.StudentMapper;
import br.ufes.ccens.repository.StudentRepository;
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
            LOG.error("Falha ao criar estudante: " + e.getMessage());
            throw new RuntimeException("Erro ao criar usuário: " + e.getMessage());
        }
    }

    public List<StudentEntity> listAll(Integer page, Integer pageSize, String name) {
        if (name != null && !name.isBlank()){
            LOG.info("Buscando estudante pelo nome: " + name);
            return studentRepository.findByName(name, page, pageSize);
        }
        LOG.info("Listando todos os estudantes");
        return studentRepository.findAll()
            .page(page, pageSize)
            .list();
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

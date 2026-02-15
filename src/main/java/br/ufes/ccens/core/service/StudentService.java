package br.ufes.ccens.core.service;

import java.util.List;
import java.util.UUID;

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

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public StudentEntity createStudent(SaveStudentRequest studentRequest) {
        try {
            var studentEntity = studentMapper.toEntity(studentRequest);
            studentRepository.persistAndFlush(studentEntity);
            return studentEntity;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar usuário: " + e.getMessage());
        }
    }

    public List<StudentEntity> listAll(Integer page, Integer pageSize) {
        return studentRepository.findAll()
            .page(page, pageSize)
            .list();
    }

    public StudentEntity findById(UUID studentId) {
        return studentRepository.findByIdOptional(studentId)
            .orElseThrow(StudentNotFoundException::new);
    }

    public StudentEntity updateStudent(UUID studentId, SaveStudentRequest studentRequest) {
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
        var student = findById(studentId);
        studentRepository.deleteById(student.getStudentId());
    }
}

package br.ufes.ccens.service;

import java.util.UUID;
import java.util.List;

import br.ufes.ccens.entity.StudentEntity;
import br.ufes.ccens.exception.StudentNotFoundException;
import br.ufes.ccens.repository.StudentRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentEntity> findByName(String name) {
        return studentRepository.findByName(name);
    }

    public long getTotalStudents() {
        return studentRepository.countStudents();
    }

    public long countStudentsByName(String name) {
        return studentRepository.countByName(name);
    }

    public StudentEntity createStudent(StudentEntity studentEntity) {
        studentRepository.persist(studentEntity);
        return studentEntity;
    }

    public List<StudentEntity> listAll(Integer page, Integer pageSize) {
        return studentRepository.findAll()
            .page(page, pageSize)
            .list();
    }

    public StudentEntity findById(UUID studentId) {
        return (StudentEntity) studentRepository.findByIdOptional(studentId)
            .orElseThrow(StudentNotFoundException::new);
    }

    public StudentEntity updateStudent(UUID studentId, StudentEntity studentEntity) {
        var student = findById(studentId);

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

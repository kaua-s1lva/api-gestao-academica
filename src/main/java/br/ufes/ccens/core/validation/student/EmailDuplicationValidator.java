package br.ufes.ccens.core.validation.student;

import br.ufes.ccens.core.exception.DuplicateResourceException;
import br.ufes.ccens.data.entity.StudentEntity;
import br.ufes.ccens.data.repository.StudentRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmailDuplicationValidator implements StudentValidationStrategy {

    private final StudentRepository studentRepository;

    public EmailDuplicationValidator(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void validate(StudentEntity student) {
        if (student.getEmail() == null || student.getEmail().isBlank()) return;

        var existing = studentRepository.find("email", student.getEmail()).firstResult();

        if (existing != null && !existing.getStudentId().equals(student.getStudentId())) {
            throw new DuplicateResourceException("O E-mail informado já está em uso por outro estudante.");
        }
    }
}
package br.ufes.ccens.core.validation.student;

import br.ufes.ccens.core.exception.DuplicateResourceException;
import br.ufes.ccens.data.entity.StudentEntity;
import br.ufes.ccens.data.repository.StudentRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CpfDuplicationValidator implements StudentValidationStrategy {

    private final StudentRepository studentRepository;

    public CpfDuplicationValidator(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void validate(StudentEntity student) {
        if (student.getCpf() == null || student.getCpf().isBlank()) return;

        var existing = studentRepository.find("cpf", student.getCpf()).firstResult();

        if (existing != null && !existing.getStudentId().equals(student.getStudentId())) {
            throw new DuplicateResourceException("cpf", "O CPF informado já está em uso por outro estudante.");
        }
    }
    
}

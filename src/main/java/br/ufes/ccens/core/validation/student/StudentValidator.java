package br.ufes.ccens.core.validation.student;

import br.ufes.ccens.data.entity.StudentEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;

@ApplicationScoped
public class StudentValidator {
    private final Instance<StudentValidationStrategy> strategies;

    public StudentValidator(Instance<StudentValidationStrategy> strategies) {
        this.strategies = strategies;
    }

    public void validate(StudentEntity student) {
        for (StudentValidationStrategy strategy : strategies) {
            strategy.validate(student);
        }
    }
}

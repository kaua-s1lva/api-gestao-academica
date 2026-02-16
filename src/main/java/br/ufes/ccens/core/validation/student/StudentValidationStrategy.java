package br.ufes.ccens.core.validation.student;

import br.ufes.ccens.data.entity.StudentEntity;

public interface StudentValidationStrategy {
    void validate(StudentEntity student);
}
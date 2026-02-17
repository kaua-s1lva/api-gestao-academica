package br.ufes.ccens.core.validation.student;

import br.ufes.ccens.core.exception.BusinessException;
import br.ufes.ccens.data.entity.StudentEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DateRangeValidator implements StudentValidationStrategy {

    @Override
    public void validate(StudentEntity student) {
        if (student.getBirthDate() != null && student.getAdmissionDate() != null) {
            if (!student.getBirthDate().isBefore(student.getAdmissionDate())) {
                throw new BusinessException("A data de nascimento deve ser anterior à data de admissão.");
            }
        }
    }
}

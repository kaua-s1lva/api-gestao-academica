package br.ufes.ccens.core.validation.academicrecord;

import br.ufes.ccens.core.exception.DuplicateResourceException;
import br.ufes.ccens.data.entity.AcademicRecordEntity;
import br.ufes.ccens.data.repository.AcademicRecordRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AcademicRecordDuplicationValidator implements AcademicRecordValidationStrategy {

    private final AcademicRecordRepository academicRecordRepository;

    public AcademicRecordDuplicationValidator(AcademicRecordRepository academicRecordRepository) {
        this.academicRecordRepository = academicRecordRepository;
    }

    @Override
    public void validate(AcademicRecordEntity academicRecord) {
        if (academicRecordRepository.hasDuplication(
                academicRecord.getStudent().getStudentId(),
                academicRecord.getDiscipline().getDisciplineId(),
                academicRecord.getSemester(),
                academicRecord.getAcademicRecordId())) {
            throw new DuplicateResourceException(
                    "Já existe um registro acadêmico para este aluno nesta disciplina e semestre.");
        }
    }
}

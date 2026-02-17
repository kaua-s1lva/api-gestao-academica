package br.ufes.ccens.core.validation.academicrecord;

import java.util.Set;
import java.util.regex.Pattern;

import br.ufes.ccens.core.exception.BusinessException;
import br.ufes.ccens.data.entity.AcademicRecordEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AcademicRecordFieldsValidator implements AcademicRecordValidationStrategy {

    private static final Pattern SEMESTER_PATTERN = Pattern.compile("^\\d{4}/\\d{1}$");
    private static final Set<String> ALLOWED_STATUS = Set.of(
            "APROVADO", "REPROVADO", "CURSANDO", "TRANCADO", "PENDENTE", "APPROVED", "FAILED");

    @Override
    public void validate(AcademicRecordEntity academicRecord) {
        validateSemester(academicRecord.getSemester());
        validateStatus(academicRecord.getStatus());
        validateGradesAndAttendance(academicRecord);
        validateObs(academicRecord.getObs());
    }

    private void validateSemester(String semester) {
        if (semester == null || !SEMESTER_PATTERN.matcher(semester).matches()) {
            throw new BusinessException("O semestre deve seguir o formato YYYY/S (ex: 2023/1).");
        }
    }

    private void validateStatus(String status) {
        if (status == null || !ALLOWED_STATUS.contains(status.toUpperCase())) {
            throw new BusinessException("Status inválido. Valores permitidos: " + String.join(", ", ALLOWED_STATUS));
        }
    }

    private void validateGradesAndAttendance(AcademicRecordEntity academicRecord) {
        if (academicRecord.getFinalGrade() != null) {
            if (academicRecord.getFinalGrade() < 0 || academicRecord.getFinalGrade() > 10) {
                throw new BusinessException("A nota final deve estar entre 0 e 10.");
            }
        }

        if (academicRecord.getAttendance() != null) {
            if (academicRecord.getAttendance() < 0 || academicRecord.getAttendance() > 100) {
                throw new BusinessException("A frequência deve estar entre 0 e 100.");
            }
        }
    }

    private void validateObs(String obs) {
        if (obs != null && obs.length() > 5000) {
            throw new BusinessException("A observação excede o tamanho máximo permitido (5000 caracteres).");
        }
    }
}

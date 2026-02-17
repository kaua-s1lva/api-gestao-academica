package br.ufes.ccens.core.validation.academicrecord;

import br.ufes.ccens.data.entity.AcademicRecordEntity;

public interface AcademicRecordValidationStrategy {
    void validate(AcademicRecordEntity academicRecord);
}

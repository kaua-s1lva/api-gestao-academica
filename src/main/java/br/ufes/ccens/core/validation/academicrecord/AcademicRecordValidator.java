package br.ufes.ccens.core.validation.academicrecord;

import br.ufes.ccens.data.entity.AcademicRecordEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;

@ApplicationScoped
public class AcademicRecordValidator {

    private final Instance<AcademicRecordValidationStrategy> strategies;

    public AcademicRecordValidator(Instance<AcademicRecordValidationStrategy> strategies) {
        this.strategies = strategies;
    }

    public void validate(AcademicRecordEntity academicRecord) {
        for (AcademicRecordValidationStrategy strategy : strategies) {
            strategy.validate(academicRecord);
        }
    }
}

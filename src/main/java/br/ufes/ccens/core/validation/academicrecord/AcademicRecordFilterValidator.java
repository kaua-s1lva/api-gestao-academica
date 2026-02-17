package br.ufes.ccens.core.validation.academicrecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import br.ufes.ccens.api.dto.response.ValidationError;
import br.ufes.ccens.core.exception.FilterValidationException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AcademicRecordFilterValidator {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "semester", "status", "finalGrade", "attendance");

    public void validate(Integer page, Integer pageSize, String sortBy, String sortDirection, String semester,
            String status, UUID studentId, UUID disciplineId) {

        List<ValidationError> errors = new ArrayList<>();

        if (page < 0) {
            errors.add(new ValidationError("page", "O número da página não pode ser negativo."));
        }

        if (pageSize <= 0) {
            errors.add(new ValidationError("pageSize", "O tamanho da página deve ser maior que zero."));
        }

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            errors.add(new ValidationError("sortBy", "Campo de ordenação inválido: " + sortBy));
        }

        if (!sortDirection.equalsIgnoreCase("asc") && !sortDirection.equalsIgnoreCase("desc")) {
            errors.add(new ValidationError("sortDirection",
                    "Direção de ordenação inválida: " + sortDirection + ". Use 'asc' ou 'desc'."));
        }

        if (semester != null && !semester.isBlank() && !semester.matches("^\\d{4}/\\d{1}$")) {
            errors.add(new ValidationError("semester", "O semestre deve seguir o padrão: YYYY/S (ex: 2023/1)"));
        }

        if (!errors.isEmpty()) {
            throw new FilterValidationException(errors);
        }
    }
}

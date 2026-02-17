package br.ufes.ccens.core.validation.student;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;

import br.ufes.ccens.api.dto.response.ValidationError;
import br.ufes.ccens.core.exception.FilterValidationException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StudentFilterValidator {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "name", "email", "registration", "admissionDate", "birthDate");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern REGISTRATION_PATTERN = Pattern.compile("^\\d{10}$");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public void validate(Integer page, Integer pageSize, String sortBy, String sortDirection,
            String registration, String cpf, String email, String admissionStart, String admissionEnd,
            String birthStart, String birthEnd) {

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

        if (registration != null && !registration.isBlank()) {
            if (!REGISTRATION_PATTERN.matcher(registration).matches()) {
                errors.add(
                        new ValidationError("registration", "Formato de matrícula inválido. Deve conter 10 dígitos."));
            }
        }

        if (cpf != null && !cpf.isBlank()) {
            CPFValidator cpfValidator = new CPFValidator();
            cpfValidator.initialize(null);
            if (!cpfValidator.isValid(cpf, null)) {
                errors.add(new ValidationError("cpf", "CPF inválido."));
            }
        }

        if (email != null && !email.isBlank()) {
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                errors.add(new ValidationError("email", "Formato de email inválido."));
            }
        }

        LocalDate parsedAdmissionStart = validateDate("admissionStart", admissionStart, errors);
        LocalDate parsedAdmissionEnd = validateDate("admissionEnd", admissionEnd, errors);
        validateDateRange("admission", parsedAdmissionStart, parsedAdmissionEnd, errors);

        LocalDate parsedBirthStart = validateDate("birthStart", birthStart, errors);
        LocalDate parsedBirthEnd = validateDate("birthEnd", birthEnd, errors);
        validateDateRange("birth", parsedBirthStart, parsedBirthEnd, errors);

        if (!errors.isEmpty()) {
            throw new FilterValidationException(errors);
        }
    }

    private LocalDate validateDate(String fieldName, String dateStr, List<ValidationError> errors) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            errors.add(new ValidationError(fieldName, "O formato da data deve ser dd-MM-yyyy."));
            return null;
        }
    }

    private void validateDateRange(String prefix, LocalDate start, LocalDate end, List<ValidationError> errors) {
        if (start != null && end != null && !start.isBefore(end)) {
            errors.add(new ValidationError(prefix + "Start",
                    "A data inicial deve ser menor que a data final."));
        }
    }
}

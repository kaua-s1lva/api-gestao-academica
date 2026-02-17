package br.ufes.ccens.core.exception;

import java.util.List;

import br.ufes.ccens.api.dto.response.ValidationError;

public class FilterValidationException extends RuntimeException {
    private final List<ValidationError> errors;

    public FilterValidationException(List<ValidationError> errors) {
        super("Validation failed for filter parameters");
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}

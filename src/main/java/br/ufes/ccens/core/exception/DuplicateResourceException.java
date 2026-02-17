package br.ufes.ccens.core.exception;

public class DuplicateResourceException extends RuntimeException {

    private final String fieldName;

    public DuplicateResourceException(String message) {
        super(message);
        this.fieldName = "unknown";
    }

    public DuplicateResourceException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}

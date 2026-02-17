package br.ufes.ccens.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConverterDataFormatImpl implements IConverterDataFormat {
    private static final DateTimeFormatter MULTI_FORMATTER = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            .toFormatter();

    @Override
    public LocalDate parse(String dateStr) {
        if (dateStr == null || dateStr.isBlank())
            return null;
        try {
            return LocalDate.parse(dateStr, MULTI_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Formato de data inválido [" + dateStr + "].");
        }
    }
}

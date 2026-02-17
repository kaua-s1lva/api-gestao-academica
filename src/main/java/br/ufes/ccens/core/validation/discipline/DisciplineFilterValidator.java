package br.ufes.ccens.core.validation.discipline;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.ufes.ccens.api.dto.response.ValidationError;
import br.ufes.ccens.core.exception.FilterValidationException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DisciplineFilterValidator {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "name", "cod", "ch", "menu", "course");

    public void validate(Integer page, Integer pageSize, String sortBy, String sortDirection, String cod, String ch) {

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

        if (cod != null && !cod.isBlank() && !cod.matches("^[A-Z]{3}[0-9]{5}$")) {
            errors.add(new ValidationError("cod",
                    "O filtro de código deve seguir o padrão: 3 letras maiúsculas seguidas de 5 números (ex: COM06853)"));
        }

        if (ch != null && !ch.isBlank() && !ch.matches("^\\d+$")) {
            errors.add(new ValidationError("ch", "O filtro de carga horária deve conter apenas números."));
        }

        if (!errors.isEmpty()) {
            throw new FilterValidationException(errors);
        }
    }
}

package br.ufes.ccens.api.dto.response;

import java.util.UUID;

public record DisciplineResponse(
        UUID disciplineId,
        String name,
        String cod,
        String ch,
        String menu,
        String course) {
}

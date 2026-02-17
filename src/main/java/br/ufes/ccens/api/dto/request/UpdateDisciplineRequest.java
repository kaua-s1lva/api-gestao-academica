package br.ufes.ccens.api.dto.request;

import jakarta.validation.constraints.Pattern;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record UpdateDisciplineRequest(
        @Pattern(regexp = ".*\\S.*", message = "O nome não pode ser vazio") @Schema(title = "Name", example = "Redes de Computadores") String name,

        @Pattern(regexp = "^[A-Z]{3}[0-9]{5}$", message = "O código deve seguir o padrão: 3 letras maiúsculas seguidas de 5 números (ex: COM06853)") @Schema(title = "Code", example = "COM10394") String cod,

        @Pattern(regexp = "^\\d+$", message = "A carga horária deve ser apenas números") @Schema(title = "Workload", example = "60") String ch,

        @Pattern(regexp = ".*\\S.*", message = "A ementa não pode ser vazia") @Schema(title = "Menu", example = "Fundamentos, arquitetura e segurança de redes de computadores.") String menu,

        @Pattern(regexp = ".*\\S.*", message = "O curso não pode ser vazio") @Schema(title = "Course", example = "Sistemas de Informação") String course) {
}

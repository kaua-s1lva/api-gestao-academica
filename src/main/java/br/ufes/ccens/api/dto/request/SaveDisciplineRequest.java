package br.ufes.ccens.api.dto.request;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SaveDisciplineRequest(
                @NotBlank(message = "O nome é obrigatório") @Schema(title = "Name", example = "Redes de Computadores") String name,

                @NotBlank(message = "O código é obrigatório") @Pattern(regexp = "^[A-Z]{3}[0-9]{5}$", message = "O código deve seguir o padrão: 3 letras maiúsculas seguidas de 5 números (ex: COM06853)") @Schema(title = "Cod", example = "COM10394") String cod,

                @NotBlank(message = "A carga horária é obrigatória") @Pattern(regexp = "^\\d+$", message = "A carga horária deve ser apenas números") @Schema(title = "Ch", example = "60") String ch,

                @Schema(title = "Menu", example = "Fundamentos, arquitetura e segurança de redes de computadores, com foco na pilha de protocolos TCP/IP e infraestrutura.") String menu,

                @NotBlank(message = "O curso é obrigatório") @Schema(title = "Course", example = "Sistemas de Informação") String course) {
}

package br.ufes.ccens.api.dto.request;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

public record SaveDisciplineRequest(
    @NotBlank(message = "O nome é obrigatório") 
    @Schema(title = "Name", example = "Redes de Computadores") 
    String name,

    @NotBlank(message = "O código é obrigatório") 
    @Schema(title = "Cod", example = "COM10394") 
    String cod,

    @NotBlank(message = "A carga horária é obrigatória") 
    @Schema(title = "Ch", example = "60") 
    String ch,

    @Schema(title = "Menu", example = "Fundamentos, arquitetura e segurança de redes de computadores, com foco na pilha de protocolos TCP/IP e infraestrutura.") 
    String menu,

    @NotBlank(message = "O curso é obrigatório") 
    @Schema(title = "Course", example = "Sistemas de Informação") 
    String course
) {}

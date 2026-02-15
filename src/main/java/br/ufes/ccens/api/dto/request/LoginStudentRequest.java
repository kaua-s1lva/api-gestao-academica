package br.ufes.ccens.api.dto.request;

public record LoginStudentRequest(
    String email,
    String password
) {}

package br.ufes.ccens.dto.request;

public record LoginStudentRequest(
    String email,
    String password
) {}

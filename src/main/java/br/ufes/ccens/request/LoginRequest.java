package br.ufes.ccens.request;

public record LoginRequest(
    String email,
    String password
) {}

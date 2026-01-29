package br.ufes.ccens.service;

import br.ufes.ccens.entity.StudentEntity;
import br.ufes.ccens.repository.StudentRepository;
import br.ufes.ccens.util.GenerateToken;
import jakarta.inject.Inject;

public class AuthService {
    
    private final GenerateToken generateToken;
    private final StudentRepository studentRepository;

    public AuthService(GenerateToken generateToken, StudentRepository studentRepository) {
        this.generateToken = generateToken;
        this.studentRepository = studentRepository;
    }

    public void login() {
        //StudentEntity student = studentRepository.findByEmail
    }
}

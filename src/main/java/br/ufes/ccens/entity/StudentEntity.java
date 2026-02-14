package br.ufes.ccens.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(name = "tb_student")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID studentId;

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @Column(unique = true, nullable = false)
    @Email(message = "Formato de email inválido")
    @NotBlank(message = "O email é obrigatório")
    private String email;

    @NotBlank(message = "A senha é obrigatório")
    private String password;

    @NotBlank(message = "O número de matrícula é obrigatório")
    private String registration;

    @NotNull(message = "A data de admissão é obrigatório")
    @Past
    private LocalDate admissionDate;

    @NotNull(message = "A data de nascimento é obrigatório")
    @Past
    private LocalDate birthDate;

    @Column(unique = true, nullable = false, length = 14)
    @CPF(message = "CPF inválido")
    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;
    
    public StudentEntity() {}

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!email.endsWith("@edu.ufes.br")) 
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getRegistration() {
        return registration;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getCpf() {
        return cpf;
    }
    
}

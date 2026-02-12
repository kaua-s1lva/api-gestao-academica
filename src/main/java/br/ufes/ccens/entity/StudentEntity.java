package br.ufes.ccens.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_student")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID studentId;

    private String name;

    private String email;

    private String password;

    private String registration;

    private LocalDate admissionDate;

    private LocalDate birthDate;

    //especificar melhor o formato do cpf
    private String cpf;

    @OneToOne
    @JoinColumn(name = "academicRecordId", nullable = false)
    private AcademicRecordEntity academicRecordEntity;
    
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

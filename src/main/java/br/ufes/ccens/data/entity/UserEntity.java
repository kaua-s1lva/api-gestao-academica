package br.ufes.ccens.data.entity;

import java.util.UUID;

import br.ufes.ccens.data.entity.enums.RoleUserEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tb_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @Column(unique = true, nullable = false)
    @Email(message = "Formato de email inválido")
    @NotBlank(message = "O email é obrigatório")
    private String email;

    @NotBlank(message = "A senha é obrigatório")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleUserEnum role;

    private Double salary;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(RoleUserEnum role) {
        this.role = role;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public UserEntity() {}

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public RoleUserEnum getRole() {
        return role;
    }

    public Double getSalary() {
        return salary;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

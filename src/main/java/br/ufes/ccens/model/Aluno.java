package br.ufes.ccens.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Aluno extends PanacheEntity {
    public String nome;
    public String matricula;
    public String email;
    public String cpf;
    public String dataNascimento;
}
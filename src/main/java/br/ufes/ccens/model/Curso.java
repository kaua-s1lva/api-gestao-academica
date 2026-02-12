package br.ufes.ccens.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Curso extends PanacheEntity {
    public String nome;
    public String codigoCurso;
    public int cargaHoraria;
    public String modalidade;      // Presencial ou EAD
    public String departamento;    // Ex.: DComp
}
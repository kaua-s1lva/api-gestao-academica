package br.ufes.ccens.data.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_academic_record")
public class AcademicRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID academicRecordId;

    //nome da disciplina
    private String subjectName;

    //nota final da disciplina obtida pelo aluno
    private Double finalGrade;

    //frequência
    private Integer attendance;

    //semestre
    private String semester;

    //A situação final do aluno (ex: "APPROVED", "RECALLED", "FAILED")
    private String status;

    @OneToOne
    @JoinColumn(name = "studentId", nullable = false)
    private StudentEntity studentId;
}

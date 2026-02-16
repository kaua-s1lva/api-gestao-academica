package br.ufes.ccens.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @jakarta.persistence.ManyToOne
    @jakarta.persistence.JoinColumn(name = "student_id")
    private StudentEntity student;

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    // Getters e Setters
    public UUID getAcademicRecordId() {
        return academicRecordId;
    }

    public void setAcademicRecordId(UUID academicRecordId) {
        this.academicRecordId = academicRecordId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public Integer getAttendance() {
        return attendance;
    }

    public void setAttendance(Integer attendance) {
        this.attendance = attendance;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package br.ufes.ccens.data.entity;

import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import br.ufes.ccens.data.entity.audity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_academic_record")
public class AcademicRecordEntity extends BaseAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID academicRecordId;

    private Integer attendance;

    private Double finalGrade;

    private String semester;

    private String status;

    @ManyToOne
    @JoinColumn(name = "disciplineId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DisciplineEntity discipline;

    @ManyToOne
    @JoinColumn(name = "studentId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StudentEntity student;

    @Column(columnDefinition = "TEXT")
    private String obs;

    public AcademicRecordEntity() {
    }

    public UUID getAcademicRecordId() {
        return academicRecordId;
    }

    public void setAcademicRecordId(UUID academicRecordId) {
        this.academicRecordId = academicRecordId;
    }

    public Integer getAttendance() {
        return attendance;
    }

    public void setAttendance(Integer attendance) {
        this.attendance = attendance;
    }

    public Double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Double finalGrade) {
        this.finalGrade = finalGrade;
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

    public DisciplineEntity getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineEntity discipline) {
        this.discipline = discipline;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}

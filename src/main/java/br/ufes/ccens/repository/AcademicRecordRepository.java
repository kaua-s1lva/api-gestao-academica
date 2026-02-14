package br.ufes.ccens.repository;

import java.util.UUID;
import java.util.List;

import br.ufes.ccens.entity.AcademicRecordEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AcademicRecordRepository implements PanacheRepositoryBase<AcademicRecordEntity, UUID> {
    public List<AcademicRecordEntity> findByStudent(UUID studentId) {
        return find("student.studentId", studentId).list();
    }

    public long countApproved(String subject) {
        return count("subjectName = ?1 and status = 'APPROVED'", subject);
    }

    public long countFailed(String subject) {
        return count("subjectName = ?1 and status = 'FAILED'", subject);
    }

    public List<AcademicRecordEntity> findGradesGreaterThan(Double grade) {
        return find("finalGrade > ?1", grade).list();
    }

    public List<AcademicRecordEntity> findByStudentName(String name) {
        return find("student.name LIKE ?1", "%" + name + "%").list();
    }

    public List<AcademicRecordEntity> findByStudentId(UUID studentId) {
        return find("student.studentId", studentId).list();
    }
}

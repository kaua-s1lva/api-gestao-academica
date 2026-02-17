package br.ufes.ccens.data.repository;

import java.util.UUID;

import br.ufes.ccens.data.entity.AcademicRecordEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AcademicRecordRepository implements PanacheRepositoryBase<AcademicRecordEntity, UUID> {
    public PanacheQuery<AcademicRecordEntity> findWithFilters(String semester, String status, UUID studentId,
            UUID disciplineId, io.quarkus.panache.common.Sort sort) {
        StringBuilder query = new StringBuilder("1=1");
        java.util.Map<String, Object> params = new java.util.HashMap<>();

        if (semester != null && !semester.isBlank()) {
            query.append(" AND semester LIKE :semester");
            params.put("semester", "%" + semester + "%");
        }
        if (status != null && !status.isBlank()) {
            query.append(" AND status = :status");
            params.put("status", status);
        }
        if (studentId != null) {
            query.append(" AND student.studentId = :studentId");
            params.put("studentId", studentId);
        }
        if (disciplineId != null) {
            query.append(" AND discipline.disciplineId = :disciplineId");
            params.put("disciplineId", disciplineId);
        }

        return find(query.toString(), sort, params);
    }

    public java.util.List<AcademicRecordEntity> findByStudentAndDiscipline(UUID studentId, UUID disciplineId) {
        StringBuilder query = new StringBuilder("student.studentId = :studentId");
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("studentId", studentId);

        if (disciplineId != null) {
            query.append(" AND discipline.disciplineId = :disciplineId");
            params.put("disciplineId", disciplineId);
        }

        return find(query.toString(), params).list();
    }

    public java.util.List<AcademicRecordEntity> findByDiscipline(UUID disciplineId) {
        return find("discipline.disciplineId", disciplineId).list();
    }

    public boolean hasDuplication(UUID studentId, UUID disciplineId, String semester, UUID excludeId) {
        StringBuilder query = new StringBuilder(
                "student.studentId = :studentId AND discipline.disciplineId = :disciplineId AND semester = :semester");
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("studentId", studentId);
        params.put("disciplineId", disciplineId);
        params.put("semester", semester);

        if (excludeId != null) {
            query.append(" AND academicRecordId != :excludeId");
            params.put("excludeId", excludeId);
        }

        return count(query.toString(), params) > 0;
    }
}

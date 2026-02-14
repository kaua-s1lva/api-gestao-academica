package br.ufes.ccens.service;

import java.util.UUID;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufes.ccens.entity.AcademicRecordEntity;
import br.ufes.ccens.repository.AcademicRecordRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AcademicRecordService {
    @Inject
    AcademicRecordRepository repository;

    public List<AcademicRecordEntity> listAll(int page, int pageSize) {
        return repository.findAll().page(page, pageSize).list();
    }

    @Transactional
    public void create(AcademicRecordEntity record) {
        repository.persist(record);
    }

    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<AcademicRecordEntity> listByStudent(UUID studentId) {
        return repository.findByStudent(studentId);
    }

    public Map<String, Long> getSubjectStats(String subject) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("approved", repository.countApproved(subject));
        stats.put("failed", repository.countFailed(subject));
        stats.put("total", repository.count("subjectName", subject));
        return stats;
    }

    public List<AcademicRecordEntity> listGradesAbove(Double grade) {
        return repository.findGradesGreaterThan(grade);
    }

    public List<AcademicRecordEntity> listByStudentName(String name) {
        return repository.findByStudentName(name);
    }
}

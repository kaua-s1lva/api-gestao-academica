package br.ufes.ccens.repository;

import java.util.UUID;
import java.util.List;

import br.ufes.ccens.entity.StudentEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StudentRepository implements PanacheRepositoryBase<StudentEntity, UUID> {
    public List<StudentEntity> findByName(String name) {
        return list("name", name);
    }

    public long countStudents() {
        return count();
    }

    public long countByName(String name) {
        return count("name", name);
    }
}

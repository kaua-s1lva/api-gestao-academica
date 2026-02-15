package br.ufes.ccens.repository;

import java.util.List;
import java.util.UUID;

import br.ufes.ccens.entity.StudentEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StudentRepository implements PanacheRepositoryBase<StudentEntity, UUID> {
    public List<StudentEntity> findByName(String name, int page, int pageSize) {
        return find("name LIKE ?1", "%" + name + "%")
                .page(page, pageSize)
                .list();
    }
}

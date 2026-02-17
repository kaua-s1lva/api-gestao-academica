package br.ufes.ccens.data.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.ufes.ccens.data.entity.DisciplineEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DisciplineRepository implements PanacheRepositoryBase<DisciplineEntity, UUID> {
    public PanacheQuery<DisciplineEntity> findWithFilters(String name, String cod, String ch, String menu,
            String course, Sort sort) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (name != null && !name.isBlank()) {
            query.append(" AND name LIKE :name");
            params.put("name", "%" + name + "%");
        }
        if (cod != null && !cod.isBlank()) {
            query.append(" AND cod LIKE :cod");
            params.put("cod", "%" + cod + "%");
        }
        if (ch != null && !ch.isBlank()) {
            query.append(" AND ch LIKE :ch");
            params.put("ch", "%" + ch + "%");
        }
        if (menu != null && !menu.isBlank()) {
            query.append(" AND menu LIKE :menu");
            params.put("menu", "%" + menu + "%");
        }
        if (course != null && !course.isBlank()) {
            query.append(" AND course LIKE :course");
            params.put("course", "%" + course + "%");
        }

        return find(query.toString(), sort, params);
    }
}

package br.ufes.ccens.data.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.ufes.ccens.data.entity.StudentEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StudentRepository implements PanacheRepositoryBase<StudentEntity, UUID> {
    public List<StudentEntity> findWithFilters(String name, String email, String registration, String cpf, LocalDate admStart, LocalDate admEnd, LocalDate birthStart, LocalDate birthEnd, int page, int pageSize) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (name != null && !name.isBlank()) {
            query.append(" AND name LIKE :name");
            params.put("name", "%" + name + "%");
        }
        if (email != null && !email.isBlank()) {
            query.append(" AND email LIKE :email");
            params.put("email", "%" + email + "%");
        }
        if (registration != null && !registration.isBlank()) {
            query.append(" AND registration LIKE :registration");
            params.put("registration", "%" + registration + "%");
        }
        if (cpf != null && !cpf.isBlank()) {
            query.append(" AND cpf LIKE :cpf");
            params.put("cpf", "%" + cpf + "%");
        }
        if (admStart != null && admEnd != null) {
            query.append(" AND admissionDate BETWEEN :start AND :end");
            params.put("start", admStart);
            params.put("end", admEnd);
        }
        if (birthStart != null && birthEnd != null) {
            query.append(" AND birthDate BETWEEN :bStart AND :bEnd");
            params.put("bStart", birthStart);
            params.put("bEnd", birthEnd);
        }

        return find(query.toString(), params).page(page, pageSize).list();
    }
}

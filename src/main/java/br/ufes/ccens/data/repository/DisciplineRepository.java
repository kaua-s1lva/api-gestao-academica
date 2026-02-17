package br.ufes.ccens.data.repository;

import java.util.UUID;

import br.ufes.ccens.data.entity.DisciplineEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DisciplineRepository implements PanacheRepositoryBase<DisciplineEntity, UUID> {
}

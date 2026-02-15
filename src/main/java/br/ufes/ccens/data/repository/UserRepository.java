package br.ufes.ccens.data.repository;

import java.util.UUID;

import br.ufes.ccens.data.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, UUID>{
    
}

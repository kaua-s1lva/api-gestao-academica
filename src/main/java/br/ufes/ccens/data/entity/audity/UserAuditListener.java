package br.ufes.ccens.data.entity.audity;

import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class UserAuditListener {

    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof Auditable auditableEntity) {
            String currentUser = getCurrentUser();
            auditableEntity.setCreatedBy(currentUser);
            auditableEntity.setUpdatedBy(currentUser);
        }
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof Auditable auditableEntity) {
            auditableEntity.setUpdatedBy(getCurrentUser());
        }
    }

    private String getCurrentUser() {
        try {
            var jwtProvider = CDI.current().select(JsonWebToken.class);

            if (jwtProvider.isResolvable()) {
                JsonWebToken jwt = jwtProvider.get();
                
                if (jwt.getSubject() != null) {
                    return jwt.getSubject();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao capturar usuário logado: " + e.getMessage());
        }
        return "system";
    
    }
}
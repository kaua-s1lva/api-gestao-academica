package br.ufes.ccens.data.db.seeder;

import org.jboss.logging.Logger;

import br.ufes.ccens.data.entity.UserEntity;
import br.ufes.ccens.data.entity.enums.RoleUserEnum;
import br.ufes.ccens.data.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ApplicationScoped
public class UserSeeder {
    
    private static final Logger LOG = Logger.getLogger(UserSeeder.class);
    
    @Inject
    UserRepository userRepository;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        LOG.info("Verificando se há dados de Usuário ADMIN no banco...");

        if (userRepository.count() > 0) {
            LOG.info("Banco de dados já contém dados de Usuário ADMIN. Preenchimento automático ignorado.");
            return;
        }

        try {
            UserEntity user = new UserEntity();
            user.setName("admin");
            user.setEmail("admin@email.com");
            user.setPassword(BcryptUtil.bcryptHash("admin123"));
            user.setRole(RoleUserEnum.ADMIN);

            userRepository.persistAndFlush(user);

            LOG.info("Usuário administrador padrão criado com sucesso.");

        } catch (ConstraintViolationException e) {
            // Isso vai te mostrar EXATAMENTE qual campo está inválido
            LOG.error("Falha na validação ao adiciona o Usuário ADMIN no banco:");
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                LOG.errorf("Campo: %s | Erro: %s", violation.getPropertyPath(), violation.getMessage());
            }
        } catch (Exception e) {
            LOG.errorf("Erro crítico ao inicializar usuários: %s", e.getMessage());
            e.printStackTrace();
        }
    }
}

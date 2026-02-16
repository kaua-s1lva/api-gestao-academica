package br.ufes.ccens.data.db.seeder;

import org.eclipse.microprofile.config.inject.ConfigProperty;

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
    @Inject
    UserRepository userRepository;

    @ConfigProperty(name = "app.seed.admin.email")
    String adminEmail;

    @ConfigProperty(name = "app.seed.admin.password")
    String adminPassword;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        System.out.println(">>> SEEDER: Iniciando verificação...");

        if (userRepository.count() > 0) {
            System.out.println(">>> SEEDER: Banco já tem dados. Pulando.");
            return;
        }

        try {
            UserEntity user = new UserEntity();
            user.setName("admin");
            user.setEmail(adminEmail);
            user.setPassword(BcryptUtil.bcryptHash(adminPassword));
            user.setRole(RoleUserEnum.ADMIN);

            userRepository.persistAndFlush(user);

            System.out.println(">>> SEEDER: Usuário criado com sucesso!");

        } catch (ConstraintViolationException e) {
            // Isso vai te mostrar EXATAMENTE qual campo está inválido
            System.out.println(">>> ERRO DE VALIDAÇÃO:");
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                System.out.println("CAMPO: " + violation.getPropertyPath() + " - ERRO: " + violation.getMessage());
            }
        } catch (Exception e) {
            System.out.println(">>> ERRO GENÉRICO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

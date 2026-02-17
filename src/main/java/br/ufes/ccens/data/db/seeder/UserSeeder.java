package br.ufes.ccens.data.db.seeder;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.ufes.ccens.core.interceptor.LogTransaction;
import br.ufes.ccens.data.entity.UserEntity;
import br.ufes.ccens.data.entity.enums.RoleUserEnum;
import br.ufes.ccens.data.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@LogTransaction
public class UserSeeder {
    @Inject
    UserRepository userRepository;

    @ConfigProperty(name = "app.seed.admin.email")
    String adminEmail;

    @ConfigProperty(name = "app.seed.admin.password")
    String adminPassword;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {

        if (userRepository.count() > 0) {
            return;
        }

        UserEntity user = new UserEntity();
        user.setName("admin");
        user.setEmail(adminEmail);
        user.setPassword(BcryptUtil.bcryptHash(adminPassword));
        user.setRole(RoleUserEnum.ADMIN);

        userRepository.persistAndFlush(user);
    }
}

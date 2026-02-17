package br.ufes.ccens.data.db.seeder;

import br.ufes.ccens.data.entity.DisciplineEntity;
import br.ufes.ccens.data.repository.DisciplineRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DisciplineSeeder {

    @Inject
    DisciplineRepository disciplineRepository;

    @Transactional
    public void onStart(@Observes @Priority(1) StartupEvent ev) {
        System.out.println(">>> SEEDER: Verificando disciplinas...");

        if (disciplineRepository.count() > 0) {
            System.out.println(">>> SEEDER: Já existem disciplinas. Pulando.");
            return;
        }

        DisciplineEntity discipline = new DisciplineEntity();
        discipline.setName("Sistemas Distribuídos");
        discipline.setCod("COM10616");
        discipline.setCh("60");
        discipline.setMenu("Fundamentos, arquitetura e implementação de sistemas distribuídos seguros e resilientes");
        discipline.setCourse("Sistemas de Informação");

        disciplineRepository.persist(discipline);
        System.out.println(">>> SEEDER: Disciplina criada com sucesso!");
    }
}

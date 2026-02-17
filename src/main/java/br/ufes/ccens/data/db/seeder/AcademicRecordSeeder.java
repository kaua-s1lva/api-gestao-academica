package br.ufes.ccens.data.db.seeder;

import java.util.List;

import br.ufes.ccens.data.entity.AcademicRecordEntity;
import br.ufes.ccens.data.entity.DisciplineEntity;
import br.ufes.ccens.data.entity.StudentEntity;
import br.ufes.ccens.data.repository.AcademicRecordRepository;
import br.ufes.ccens.data.repository.DisciplineRepository;
import br.ufes.ccens.data.repository.StudentRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AcademicRecordSeeder {

    @Inject
    AcademicRecordRepository academicRecordRepository;

    @Inject
    StudentRepository studentRepository;

    @Inject
    DisciplineRepository disciplineRepository;

    @Transactional
    public void onStart(@Observes @Priority(2) StartupEvent ev) {
        System.out.println(">>> SEEDER: Verificando registros acadêmicos...");

        if (academicRecordRepository.count() > 0) {
            System.out.println(">>> SEEDER: Já existem registros acadêmicos. Pulando.");
            return;
        }

        // Tenta buscar um estudante e uma disciplina existentes
        List<StudentEntity> students = studentRepository.listAll();
        List<DisciplineEntity> disciplines = disciplineRepository.listAll();

        if (students.isEmpty() || disciplines.isEmpty()) {
            System.out.println(
                    ">>> SEEDER: Não há estudantes ou disciplinas suficientes para criar registros acadêmicos.");
            return;
        }

        StudentEntity student = students.get(0);
        DisciplineEntity discipline = disciplines.get(0);

        AcademicRecordEntity record = new AcademicRecordEntity();
        record.setStudent(student);
        record.setDiscipline(discipline);
        record.setAttendance(100);
        record.setFinalGrade(10.0);
        record.setSemester("2025/2");
        record.setStatus("Aprovado");
        record.setObs("Aluno lindo e exemplar.");

        academicRecordRepository.persist(record);
        System.out.println(">>> SEEDER: Registro acadêmico criado com sucesso!");
    }
}

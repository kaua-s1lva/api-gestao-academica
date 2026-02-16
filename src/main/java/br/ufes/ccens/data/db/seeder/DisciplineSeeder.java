package br.ufes.ccens.data.db.seeder;

import br.ufes.ccens.data.repository.StudentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DisciplineSeeder {

    @Inject
    StudentRepository studentRepository;

    // @Transactional
    // private void onStart(@Observes StartupEvent ev) {
    //     if (studentRepository.count() == 0) {
    //         StudentEntity student = new StudentEntity();
    //         student.setName("maik mau fredo");
    //         student.setEmail("maik.maufredo@edu.ufes.br");
    //         student.setPassword("maik123");
    //         student.setAdmissionDate(LocalDate.parse("2022-10-01"));
    //         student.setRegistration("2022200556");
    //         student.setBirthDate(LocalDate.parse("2000-01-01"));
    //         student.setCpf("12345678912");

    //         student.setPassword(BcryptUtil.bcryptHash(student.getPassword()));

    //         studentRepository.persist(student);
    //     }
    // }
    /*









    //especificar melhor o formato do cpf
    private String cpf; */
}

package br.ufes.ccens.data.db.seeder;

import java.time.LocalDate;

import br.ufes.ccens.data.entity.StudentEntity;
import br.ufes.ccens.data.repository.StudentRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ApplicationScoped
public class StudentSeeder {

    @Inject
    StudentRepository studentRepository;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        System.out.println(">>> SEEDER: Iniciando verificação...");

        if (studentRepository.count() > 0) {
            System.out.println(">>> SEEDER: Banco já tem dados. Pulando.");
            return;
        }

        try {
            StudentEntity student = new StudentEntity();
            student.setName("maik mau fredo");
            student.setEmail("maik.maufredo@edu.ufes.br");
            student.setPassword("maik123");
            student.setAdmissionDate(LocalDate.parse("2022-10-01"));
            student.setRegistration("2022200556");
            student.setBirthDate(LocalDate.parse("2000-01-01"));
            
            // CPF Válido para teste (Gerado aleatoriamente)
            student.setCpf("61328198006");

            // Hash da senha
            student.setPassword(BcryptUtil.bcryptHash(student.getPassword()));

            // Persiste e FORÇA o envio pro banco agora para testar erros
            studentRepository.persist(student);
            studentRepository.flush(); 

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
    /*









    //especificar melhor o formato do cpf
    private String cpf; */
}

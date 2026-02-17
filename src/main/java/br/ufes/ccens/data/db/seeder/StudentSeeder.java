package br.ufes.ccens.data.db.seeder;

import java.time.LocalDate;

import br.ufes.ccens.data.entity.StudentEntity;
import br.ufes.ccens.data.repository.StudentRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.Priority;
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
    public void onStart(@Observes @Priority(1) StartupEvent ev) {
        System.out.println(">>> SEEDER: Iniciando verificação...");

        if (studentRepository.count() > 0) {
            System.out.println(">>> SEEDER: Banco já tem dados. Pulando.");
            return;
        }

        try {
            StudentEntity student = new StudentEntity();
            student.setName("Kauã de Souza da Silva");
            student.setEmail("kaua.silva@edu.ufes.br");
            student.setAdmissionDate(LocalDate.parse("2022-10-01"));
            student.setRegistration("2022200418");
            student.setBirthDate(LocalDate.parse("2000-01-01"));

            // CPF Válido para teste (Gerado aleatoriamente)
            student.setCpf("61328198006");

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
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * //especificar melhor o formato do cpf
     * private String cpf;
     */
}

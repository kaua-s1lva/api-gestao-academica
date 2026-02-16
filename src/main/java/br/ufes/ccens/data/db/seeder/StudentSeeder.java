package br.ufes.ccens.data.db.seeder;

import java.time.LocalDate;

import org.jboss.logging.Logger;

import br.ufes.ccens.core.service.StudentService;
import br.ufes.ccens.data.entity.StudentEntity;
import br.ufes.ccens.data.repository.StudentRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ApplicationScoped
public class StudentSeeder {

    private static final Logger LOG = Logger.getLogger(StudentSeeder.class);
    @Inject
    StudentRepository studentRepository;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        LOG.info("Verificando se há dados de Estudantes no banco...");

        if (studentRepository.count() > 0) {
            LOG.info("Banco de dados já contém dados de estudantes. Preenchimento automático ignorado.");
            return;
        }

        try {
            StudentEntity student = new StudentEntity();
            student.setName("maik mau fredo");
            student.setEmail("maik.maufredo@edu.ufes.br");
            student.setAdmissionDate(LocalDate.parse("2022-10-01"));
            student.setRegistration("2022200556");
            student.setBirthDate(LocalDate.parse("2000-01-01"));
            
            // CPF Válido para teste (Gerado aleatoriamente)
            student.setCpf("61328198006");

            // Persiste e FORÇA o envio pro banco agora para testar erros
            studentRepository.persist(student);
            studentRepository.flush(); 

            LOG.info("Estudante criado com sucesso no banco de dados.");

        } catch (ConstraintViolationException e) {
            // Isso vai te mostrar EXATAMENTE qual campo está inválido
            LOG.error("Falha na validação ao adiciona o Estudante no banco:");
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                LOG.errorf("Campo: %s | Mensagem de Erro: %s", violation.getPropertyPath(), violation.getMessage());
            }
        } catch (Exception e) {
            LOG.errorf("Erro genérico: %s", e.getMessage());
        }
    }
    /*









    //especificar melhor o formato do cpf
    private String cpf; */
}

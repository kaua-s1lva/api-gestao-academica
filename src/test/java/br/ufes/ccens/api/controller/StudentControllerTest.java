package br.ufes.ccens.api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ufes.ccens.data.repository.StudentRepository;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class StudentControllerTest {

    @Inject
    StudentRepository studentRepository;

    @BeforeEach
    @Transactional
    void cleanUp() {
        studentRepository.deleteAll();
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"}) 
    public void testListAllStudents() {
        String studentPayload = """
                {
                  "name": "João da Silva",
                  "email": "joao.silva@example.com",
                  "registration": "2023100150",
                  "admissionDate": "01-01-2021",
                  "birthDate": "01-01-2001",
                  "cpf": "69855070003"
                }
                """;

        given()
          .contentType(ContentType.JSON)
          .body(studentPayload)
        .when()
          .post("/students")
        .then()
          .statusCode(201); // Garante que o usuário foi criado

        // 2. VALIDAÇÃO: Agora sim, chamamos a listagem esperando o Status 200
        given()
          .when()
          .get("/students?page=0&pageSize=10")
        .then()
          .statusCode(200)
          .body("page", is(0))
          .body("pageSize", is(10))
          .body("content", notNullValue());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"}) // Injeta a role exigida [cite: 240]
    public void testCreateStudentSuccess() {
        // Testa criação com status 201 [cite: 234, 235]
        String studentPayload = """
                {
                  "name": "João da Silva",
                  "email": "joao.silva@example.com",
                  "registration": "2023100150",
                  "admissionDate": "01-01-2021",
                  "birthDate": "01-01-2001",
                  "cpf": "69855070003"
                }
                """;

        given()
          .contentType(ContentType.JSON)
          .body(studentPayload)
        .when()
          .post("/students")
        .then()
          .log().all()
          .statusCode(201)
          .body("studentId", notNullValue());
    }

    @Test
    @TestSecurity(user = "user")
    public void testFindStudentByIdNotFound() {
        // Testa busca de ID inexistente esperando 404 [cite: 251, 257]
        UUID fakeId = UUID.randomUUID();
        
        given()
          .when()
          .get("/students/" + fakeId)
        .then()
          .statusCode(404)
          .body("message", is("Estudante não encontrado com o ID fornecido.")); // ApiErrorResponse [cite: 258, 259]
    }
}
package br.ufes.ccens;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;

@QuarkusTest
public class StudentResourceTest {

    @Test
    @TestSecurity(user = "admin", roles = "ADMIN")
    public void testListarAlunos() {
        given()
            .when().get("/students")
            .then()
            .statusCode(200); // Valida o status 200 OK [cite: 110]
    }

    @Test
    @TestSecurity(user = "admin", roles = "ADMIN")
    public void testCriarAlunoValido() { // Caso obrigatório 
        String json = "{\"name\": \"Mik\", \"email\": \"mik@edu.ufes.br\", \"admissionDate\": \"2022-10-01\", \"registration\": \"2020001\", \"birthDate\": \"2000-01-01\", \"cpf\": \"61328198006\"}";
        
        given()
          .contentType(ContentType.JSON)
          .body(json)
          .when().post("/students")
          .then()
             .statusCode(201); // Valida o status 201 Created [cite: 111]
    }
}
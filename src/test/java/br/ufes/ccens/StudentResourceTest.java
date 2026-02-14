package br.ufes.ccens;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class StudentResourceTest {

    @Test
    public void testListarAlunos() {
        given()
            .when().get("/students")
            .then()
            .statusCode(200); // Valida o status 200 OK [cite: 110]
    }

    @Test
    public void testCriarAlunoValido() { // Caso obrigatório 
        String json = "{\"nome\": \"Mik\", \"matricula\": \"2025001\", \"email\": \"mik@ufes.br\"}";
        
        given()
          .contentType(ContentType.JSON)
          .body(json)
          .when().post("/alunos")
          .then()
             .statusCode(201); // Valida o status 201 Created [cite: 111]
    }
}
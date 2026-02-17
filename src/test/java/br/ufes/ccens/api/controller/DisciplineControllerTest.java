package br.ufes.ccens.api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class DisciplineControllerTest {

    @Test
    @TestSecurity(user = "user")
    public void testListAllDisciplines() {
        // Testa a listagem paginada de disciplinas [cite: 164, 169]
        given()
          .when()
          .get("/disciplines?page=0&pageSize=10")
        .then()
          .statusCode(200)
          .body("content", notNullValue());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"}) // Permissão necessária [cite: 187]
    public void testCreateDisciplineSuccess() {
        // Testa criação com dados válidos [cite: 180, 181]
        String disciplinePayload = """
                {
                  "name": "Redes de Computadores",
                  "cod": "TES00123",
                  "ch": "60",
                  "menu": "Camadas de rede, TCP/IP e roteamento.",
                  "course": "Sistemas de Informação"
                }
                """;

        given()
          .contentType(ContentType.JSON)
          .body(disciplinePayload)
        .when()
          .post("/disciplines")
        .then()
          .statusCode(201)
          .body("cod", is("TES00123"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"}) // Permissão de deleção 
    public void testDeleteDisciplineNotFound() {
        // Tenta deletar ID inválido e espera 404 [cite: 214]
        UUID fakeId = UUID.randomUUID();

        given()
          .when()
          .delete("/disciplines/" + fakeId)
        .then()
          .statusCode(404);
    }
}
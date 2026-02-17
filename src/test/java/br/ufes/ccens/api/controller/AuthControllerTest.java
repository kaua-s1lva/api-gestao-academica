package br.ufes.ccens.api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class AuthControllerTest {

    @Test
    public void testLoginSuccess() {
        // Testa o endpoint /auth/login com credenciais válidas esperando status 200 [cite: 144, 146]
        String loginPayload = """
                {
                  "email": "admin@email.com",
                  "password": "admin123"
                }
                """;

        given()
          .contentType(ContentType.JSON)
          .body(loginPayload)
        .when()
          .post("/auth/login")
        .then()
          .statusCode(200)
          .body("token", notNullValue()); // Deve retornar o TokenResponse [cite: 147]
    }

    @Test
    public void testLoginInvalidCredentials() {
        // Testa falha no login esperando status 401 [cite: 150]
        String loginPayload = """
                {
                  "email": "errado@email.com",
                  "password": "senhaerrada"
                }
                """;

        given()
          .contentType(ContentType.JSON)
          .body(loginPayload)
        .when()
          .post("/auth/login")
        .then()
          .statusCode(401); // Invalid Credentials [cite: 150]
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    public void testRegisterUserSuccess() {
        // Testa o registro de usuário esperando status 201 [cite: 155, 157]
        String registerPayload = """
                {
                  "name": "Novo Usuário",
                  "email": "teste.registro@email.com",
                  "password": "senha123",
                  "role": "PROFESSOR",
                  "salary": 5000.0
                }
                """;

        given()
          .contentType(ContentType.JSON)
          .body(registerPayload)
        .when()
          .post("/auth/register")
        .then()
          .statusCode(201)
          .body("name", is("Novo Usuário"))
          .body("role", is("PROFESSOR")); // Deve retornar o RegisterUserResponse [cite: 158]
    }
}
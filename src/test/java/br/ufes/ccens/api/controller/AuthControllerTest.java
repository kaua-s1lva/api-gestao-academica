package br.ufes.ccens.api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class AuthControllerTest {

    @ConfigProperty(name = "app.seed.admin.email")
    String adminEmail;

    @ConfigProperty(name = "app.seed.admin.password")
    String adminPassword;

    @Test
    public void testLoginSuccess() {
        String loginPayload = """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(adminEmail, adminPassword);

        given()
          .contentType(ContentType.JSON)
          .body(loginPayload)
        .when()
          .post("/auth/login")
        .then()
          .statusCode(200)
          .body("token", notNullValue());
    }

    @Test
    public void testLoginInvalidCredentials() {
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
          .statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    public void testRegisterUserSuccess() {
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
          .body("role", is("PROFESSOR"));
    }
}
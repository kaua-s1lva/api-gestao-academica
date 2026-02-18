package br.ufes.ccens.api.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class AcademicRecordControllerTest {

    @Test
    @TestSecurity(user = "user")
    public void testListAllAcademicRecords() {
        // Testa listagem paginada com ordenação default (semester asc) [cite: 59, 60]
        given()
          .when()
          .get("/academic-records?page=0&pageSize=10")
        .then()
          .statusCode(200)
          .body("content", notNullValue()); // Page of Academic Records [cite: 61, 62]
    }

    @Test
    @TestSecurity(user = "user")
    public void testListByStudent() {
        // Testa o filtro específico por UUID de estudante [cite: 97, 98]
        String studentId = "f8740ebb-0bcc-11f1-a5cb-002308b492c4";
        
        given()
          .when()
          .get("/academic-records/student/" + studentId)
        .then()
          // Pode retornar 200 (com lista vazia) ou 404 dependendo da sua regra de negócio
          .statusCode(200); 
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    public void testCreateAcademicRecordValidationFail() {
        // Testa criação enviando nota final acima do máximo (10) esperando Bad Request [cite: 82, 83]
        String invalidPayload = """
                {
                  "studentId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                  "disciplineId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                  "attendance": 100,
                  "finalGrade": 11.5, 
                  "semester": "2023/1",
                  "status": "Aprovado"
                }
                """; // finalGrade deve ser <= 10 [cite: 18]

        given()
          .contentType(ContentType.JSON)
          .body(invalidPayload)
        .when()
          .post("/academic-records")
        .then()
          .statusCode(400); // Bad Request (Validation Error) [cite: 82]
    }
}
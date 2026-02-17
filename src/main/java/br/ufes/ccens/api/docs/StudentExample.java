package br.ufes.ccens.api.docs;

public class StudentExample {

  public static final String BAD_REQUEST_RESPONSE = """
      {
        "status": 400,
        "message": "Invalid CPF format",
        "errors": [
          {
            "field": "cpf",
            "reason": "cpf must be in the format XXX.XXX.XXX-XX"
          }
        ]
      }
      """;

  public static final String NOT_FOUND_RESPONSE = """
      {
        "status": 404,
        "message": "Students not found",
        "errors": []
      }
      """;

  public static final String INTERNAL_SERVER_ERROR_RESPONSE = """
      {
        "status": 500,
        "message": "Internal Server Error",
        "errors": []
      }
      """;

  public static final String UPDATE_SUCCESS_RESPONSE = """
      {
        "studentId": "f8740ebb-0bcc-11f1-a5cb-002308b492c4",
        "name": "Nome do Estudante Atualizado",
        "email": "email.atualizado@exemplo.com",
        "registration": "2021200000",
        "admissionDate": "01-01-2021",
        "birthDate": "01-01-2000"
      }
      """;

  public static final String STUDENT_NOT_FOUND_RESPONSE = """
      {
        "status": 404,
        "message": "Student not found",
        "errors": []
      }
      """;

  public static final String UPDATE_BAD_REQUEST_RESPONSE = """
      {
        "status": 400,
        "message": "Validation Error",
        "errors": [
          {
            "field": "email",
            "reason": "Formato de email inválido"
          }
        ]
      }
      """;

  public static final String FIND_BY_ID_RESPONSE = """
      {
        "studentId": "f8740ebb-0bcc-11f1-a5cb-002308b492c4",
        "name": "Nome do Estudante",
        "email": "email@exemplo.com",
        "registration": "2021200000",
        "admissionDate": "01-01-2021",
        "birthDate": "01-01-2000"
      }
      """;

  public static final String INVALID_UUID_RESPONSE = """
      {
        "status": 400,
        "message": "Bad Request",
        "errors": [
          {
            "field": "id",
            "reason": "Invalid UUID format"
          }
        ]
      }
      """;

  public static final String DELETE_SUCCESS_RESPONSE = """
      {
        "message": "Student successfully deleted!"
      }
      """;

  private StudentExample() {
    // Private constructor to hide the implicit public one
  }
}

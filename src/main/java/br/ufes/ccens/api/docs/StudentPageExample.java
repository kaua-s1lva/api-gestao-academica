package br.ufes.ccens.api.docs;

public class StudentPageExample {

  public static final String LIST_ALL_RESPONSE = """
      {
        "content": [
          {
            "studentId": "f8740ebb-0bcc-11f1-a5cb-002308b492c4",
            "name": "Nome do Estudante",
            "email": "email@exemplo.com",
            "registration": "2021200000",
            "admissionDate": "01-01-2021",
            "birthDate": "01-01-2000"
          }
        ],
        "page": 0,
        "pageSize": 10,
        "totalElements": 1,
        "totalPages": 1
      }
      """;

  public static final String BAD_REQUEST_RESPONSE = """
      {
        "status": 400,
        "message": "Invalid date format",
        "errors": [
          {
            "field": "admissionStart",
            "reason": "Date must be in the format dd-MM-yyyy"
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

  private StudentPageExample() {
    // Private constructor to hide the implicit public one
  }
}

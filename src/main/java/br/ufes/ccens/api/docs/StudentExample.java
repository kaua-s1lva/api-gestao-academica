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

  private StudentExample() {
    // Private constructor to hide the implicit public one
  }
}

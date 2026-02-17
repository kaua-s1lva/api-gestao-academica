package br.ufes.ccens.api.docs;

public class DisciplineExample {

  public static final String LIST_ALL_RESPONSE = """
      {
        "content": [
          {
            "id": "f8740ebb-0bcc-11f1-a5cb-002308b492c4",
            "name": "Redes de Computadores",
            "cod": "COM10394",
            "ch": "60",
            "menu": "Camadas de rede, TCP/IP e roteamento.",
            "course": "Sistemas de Informação"
          }
        ],
        "page": 0,
        "pageSize": 10,
        "totalElements": 1,
        "totalPages": 1
      }
      """;

  public static final String FIND_BY_ID_RESPONSE = """
      {
        "id": "f8740ebb-0bcc-11f1-a5cb-002308b492c4",
        "name": "Redes de Computadores",
        "cod": "COM10394",
        "ch": "60",
        "menu": "Camadas de rede, TCP/IP e roteamento.",
        "course": "Sistemas de Informação"
      }
      """;

  public static final String CREATE_RESPONSE = """
      {
        "id": "f8740ebb-0bcc-11f1-a5cb-002308b492c4",
        "name": "Redes de Computadores",
        "cod": "COM10394",
        "ch": "60",
        "menu": "Camadas de rede, TCP/IP e roteamento.",
        "course": "Sistemas de Informação"
      }
      """;

  public static final String UPDATE_SUCCESS_RESPONSE = """
      {
        "id": "f8740ebb-0bcc-11f1-a5cb-002308b492c4",
        "name": "Redes de Computadores",
        "cod": "COM10394",
        "ch": "60",
        "menu": "Camadas de rede, TCP/IP e roteamento.",
        "course": "Sistemas de Informação"
      }
      """;

  public static final String DELETE_SUCCESS_RESPONSE = """
      {
        "message": "Discipline successfully deleted!"
      }
      """;

  public static final String BAD_REQUEST_RESPONSE = """
      {
        "status": 400,
        "message": "Validation Error",
        "errors": [
          {
            "field": "name",
            "reason": "O nome é obrigatório"
          }
        ]
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

  public static final String NOT_FOUND_RESPONSE = """
      {
        "status": 404,
        "message": "Discipline not found",
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

  private DisciplineExample() {
    // Private constructor
  }
}

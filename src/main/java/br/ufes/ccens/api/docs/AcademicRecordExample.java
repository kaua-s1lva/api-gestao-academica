package br.ufes.ccens.api.docs;

public class AcademicRecordExample {

  public static final String PAGE_RESPONSE = """
      {
        "content": [
          {
            "academicRecordId": "3ada8b23-0bff-11f1-a5cb-002308b492c4",
            "student": {
              "studentId": "f8741f21-0bcc-11f1-a5cb-002308b492c4",
              "name": "nome do aluno",
              "email": "aluno@example",
              "registration": "2021200007",
              "admissionDate": "01-01-2010",
              "birthDate": "01-01-2000"
            },
            "discipline": {
              "disciplineId": "3139d070-0bca-11f1-a5cb-002308b492c4",
              "name": "Cálculo A",
              "cod": "MPA06839",
              "ch": "90",
              "menu": "Limites, derivadas e integrais de uma variável.",
              "course": "Sistemas de Informação"
            },
            "attendance": 100,
            "finalGrade": 9,
            "semester": "2019/1",
            "status": "Aprovado",
            "obs": null
          }
        ],
        "page": 0,
        "pageSize": 10,
        "totalElements": 1,
        "totalPages": 1
      }
      """;

  public static final String LIST_ALL_RESPONSE = """
      [
        {
          "id": "e2f1c8a9-4b13-4c12-a7d6-7c9f8e4a9b63",
          "studentId": "f8740ebb-0bcc-11f1-a5cb-002308b492c4",
          "disciplineId": "a9d8c7b6-e5f4-3d21-c0b9-8a7e6f5d4c32",
          "grade": 9.5,
          "semester": "2023/1",
          "status": "APPROVED"
        }
      ]
      """;

  public static final String FIND_BY_ID_RESPONSE = """
      {
        "academicRecordId": "3ada8b23-0bff-11f1-a5cb-002308b492c4",
        "student": {
          "studentId": "f8741f21-0bcc-11f1-a5cb-002308b492c4",
          "name": "nome do aluno",
          "email": "aluno@example",
          "registration": "2021200007",
          "admissionDate": "01-01-2010",
          "birthDate": "01-01-2000"
        },
        "discipline": {
          "disciplineId": "3139d070-0bca-11f1-a5cb-002308b492c4",
          "name": "Cálculo A",
          "cod": "MPA06839",
          "ch": "90",
          "menu": "Limites, derivadas e integrais de uma variável.",
          "course": "Sistemas de Informação"
        },
        "attendance": 100,
        "finalGrade": 9,
        "semester": "2019/1",
        "status": "Aprovado",
        "obs": null
      }
      """;

  public static final String CREATE_RESPONSE = """
      {
        "academicRecordId": "3ada8b23-0bff-11f1-a5cb-002308b492c4",
        "student": {
          "studentId": "f8741f21-0bcc-11f1-a5cb-002308b492c4",
          "name": "nome do aluno",
          "email": "aluno@example",
          "registration": "2021200007",
          "admissionDate": "01-01-2010",
          "birthDate": "01-01-2000"
        },
        "discipline": {
          "disciplineId": "3139d070-0bca-11f1-a5cb-002308b492c4",
          "name": "Cálculo A",
          "cod": "MPA06839",
          "ch": "90",
          "menu": "Limites, derivadas e integrais de uma variável.",
          "course": "Sistemas de Informação"
        },
        "attendance": 100,
        "finalGrade": 9,
        "semester": "2019/1",
        "status": "Aprovado",
        "obs": null
      }
      """;

  public static final String UPDATE_SUCCESS_RESPONSE = """
      {
        "academicRecordId": "3ada8b23-0bff-11f1-a5cb-002308b492c4",
        "student": {
          "studentId": "f8741f21-0bcc-11f1-a5cb-002308b492c4",
          "name": "nome do aluno",
          "email": "aluno@example",
          "registration": "2021200007",
          "admissionDate": "01-01-2010",
          "birthDate": "01-01-2000"
        },
        "discipline": {
          "disciplineId": "3139d070-0bca-11f1-a5cb-002308b492c4",
          "name": "Cálculo A",
          "cod": "MPA06839",
          "ch": "90",
          "menu": "Limites, derivadas e integrais de uma variável.",
          "course": "Sistemas de Informação"
        },
        "attendance": 100,
        "finalGrade": 9,
        "semester": "2019/1",
        "status": "Aprovado",
        "obs": null
      }
      """;

  public static final String DELETE_SUCCESS_RESPONSE = """
      {
        "message": "Academic Record successfully deleted!"
      }
      """;

  public static final String BAD_REQUEST_RESPONSE = """
      {
        "status": 400,
        "message": "Validation Error",
        "errors": [
          {
            "field": "grade",
            "reason": "Note must be between 0 and 10"
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
        "message": "Academic Record not found",
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

  private AcademicRecordExample() {
    // Private constructor
  }
}

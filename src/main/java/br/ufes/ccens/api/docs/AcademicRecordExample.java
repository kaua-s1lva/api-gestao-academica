package br.ufes.ccens.api.docs;

public class AcademicRecordExample {

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
              "id": "e2f1c8a9-4b13-4c12-a7d6-7c9f8e4a9b63",
              "studentId": "f8740ebb-0bcc-11f1-a5cb-002308b492c4",
              "disciplineId": "a9d8c7b6-e5f4-3d21-c0b9-8a7e6f5d4c32",
              "grade": 9.5,
              "semester": "2023/1",
              "status": "APPROVED"
            }
            """;

    public static final String CREATE_RESPONSE = """
            {
              "id": "e2f1c8a9-4b13-4c12-a7d6-7c9f8e4a9b63",
              "studentId": "f8740ebb-0bcc-11f1-a5cb-002308b492c4",
              "disciplineId": "a9d8c7b6-e5f4-3d21-c0b9-8a7e6f5d4c32",
              "grade": 9.5,
              "semester": "2023/1",
              "status": "APPROVED"
            }
            """;

    public static final String UPDATE_SUCCESS_RESPONSE = """
            {
              "id": "e2f1c8a9-4b13-4c12-a7d6-7c9f8e4a9b63",
              "studentId": "f8740ebb-0bcc-11f1-a5cb-002308b492c4",
              "disciplineId": "a9d8c7b6-e5f4-3d21-c0b9-8a7e6f5d4c32",
              "grade": 10.0,
              "semester": "2023/1",
              "status": "APPROVED"
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

package br.ufes.ccens.api.docs;

public class AuthExample {

    public static final String LOGIN_REQUEST = """
            {
              "email": "admin@email.com",
              "password": "admin123"
            }
            """;

    public static final String TOKEN_RESPONSE = """
            {
              "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c..."
            }
            """;

    public static final String INVALID_CREDENTIALS_RESPONSE = """
            {
            "status": 401,
            "message": "Invalid Credentials",
            "errors": [
              {
                "field": "email",
                "reason": "E-mail inválido"
              },
              {
                "field": "senha",
                "reason": "Senha inválida"
              }
            ]
            }
            """;

    public static final String REGISTER_REQUEST = """
            {
              "name": "Novo Usuário",
              "email": "novo.usuario@email.com",
              "password": "senha123",
              "role": "PROFESSOR",
              "salary": 5000.0
            }
            """;

    public static final String REGISTER_RESPONSE = """
            {
              "id": "e2f1c8a9-4b13-4c12-a7d6-7c9f8e4a9b63",
              "name": "Novo Usuário",
              "email": "novo.usuario@email.com",
              "role": "PROFESSOR",
              "salary": 5000.0
            }
            """;

    public static final String BAD_REQUEST_RESPONSE = """
            {
            "status": 400,
            "message": "Bad Request",
            "errors": [
              {
                "field": "email",
                "reason": "O E-mail informado já está em uso por outro usuário."
              }
            ]
            }
            """;

    public static final String INTERNAL_SERVER_ERROR_RESPONSE = """
            {
            "status": 500,
            "message": "Internal Server Error",
            "errors": []
            }
            """;
}

package br.ufes.ccens.core.config;

import org.eclipse.microprofile.openapi.annotations.Components;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import br.ufes.ccens.api.dto.response.ApiErrorResponse;
import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(title = "Academic Management API", version = "1.0.0", description = "API for managing academic records, disciplines, and students."), components = @Components(responses = {
    @APIResponse(name = "Success", responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Success Example", value = """
        {
          "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
          "name": "Exemplo de Recurso",
          ...
        }
        """))),
    @APIResponse(name = "Created", responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Created Example", value = """
        {
          "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
          "name": "Novo Recurso",
          ...
        }
        """))),
    @APIResponse(name = "BadRequest", responseCode = "400", description = "Bad Request (Validation Error)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Validation Error", value = """
        {
          "status": 400,
          "message": "Erro de validação de dados.",
          "errors": [
            {
              "field": "cpf",
              "reason": "Este CPF já está em uso."
            }
          ]
        }
        """))),
    @APIResponse(name = "NotFound", responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Not Found Error", value = """
        {
          "status": 404,
          "message": "Registro não encontrado.",
          "errors": []
        }
        """))),
    @APIResponse(name = "InternalServerError", responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = """
        {
          "status": 500,
          "message": "Erro interno do servidor.",
          "errors": []
        }
        """)))
}))
public class OpenApiConfig extends Application {
}

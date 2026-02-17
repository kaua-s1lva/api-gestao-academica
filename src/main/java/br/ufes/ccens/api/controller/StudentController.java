package br.ufes.ccens.api.controller;

import java.util.Map;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import br.ufes.ccens.api.docs.StudentExample;
import br.ufes.ccens.api.docs.StudentPageExample;
import br.ufes.ccens.api.dto.request.SaveStudentRequest;
import br.ufes.ccens.api.dto.request.UpdateStudentRequest;
import br.ufes.ccens.api.dto.response.ApiErrorResponse;
import br.ufes.ccens.api.dto.response.PageResponse;
import br.ufes.ccens.api.dto.response.StudentResponse;
import br.ufes.ccens.core.service.StudentService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GET
    @APIResponse(responseCode = "200", description = "List of students", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class), examples = @ExampleObject(name = "Page of Students", value = StudentPageExample.LIST_ALL_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Invalid Date Example", value = StudentPageExample.BAD_REQUEST_RESPONSE)))
    @APIResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Not Found Example", value = StudentPageExample.NOT_FOUND_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error Example", value = StudentExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response listAll(
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("pageSize") @DefaultValue("10") Integer pageSize,
            @QueryParam("sortBy") @DefaultValue("name") String sortBy,
            @QueryParam("sortDirection") @DefaultValue("asc") String sortDir,
            @QueryParam("name") String name,
            @QueryParam("email") String email,
            @QueryParam("registration") String registration,
            @QueryParam("cpf") String cpf,
            @QueryParam("admissionStart") String admStart,
            @QueryParam("admissionEnd") String admEnd,
            @QueryParam("birthStart") String birthStart,
            @QueryParam("birthEnd") String birthEnd) {

        var students = studentService.listAll(page, pageSize, sortBy, sortDir, name, email, registration, cpf,
                admStart, admEnd, birthStart, birthEnd);
        return Response.ok(students).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    @APIResponse(responseCode = "201", description = "Student created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class)))
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Invalid CPF Example", value = StudentExample.BAD_REQUEST_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error Example", value = StudentExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response createStudent(@Valid SaveStudentRequest studentRequest) {
        return Response.status(Response.Status.CREATED)
                .entity(studentService.createStudent(studentRequest))
                .build();
    }

    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", description = "Student found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class), examples = @ExampleObject(name = "Student Found", value = StudentExample.FIND_BY_ID_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request (Invalid UUID)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Invalid UUID", value = StudentExample.INVALID_UUID_RESPONSE)))
    @APIResponse(responseCode = "404", description = "Student not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Not Found", value = StudentExample.STUDENT_NOT_FOUND_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = StudentExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response findStudentById(@PathParam("id") UUID studentId) {
        return Response.ok(studentService.findById(studentId)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @APIResponse(responseCode = "200", description = "Student updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class), examples = @ExampleObject(name = "Updated Student", value = StudentExample.UPDATE_SUCCESS_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Validation Error", value = StudentExample.UPDATE_BAD_REQUEST_RESPONSE)))
    @APIResponse(responseCode = "404", description = "Student not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Not Found", value = StudentExample.STUDENT_NOT_FOUND_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = StudentExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response updateStudent(
            @PathParam("id") UUID studentId,
            @Valid UpdateStudentRequest studentRequest) {
        return Response.ok(studentService.updateStudent(studentId, studentRequest)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @APIResponse(responseCode = "200", description = "Student deleted", content = @Content(mediaType = "application/json",schema = @Schema(example = "{\"message\": \"Student successfully deleted!\"}"), examples = @ExampleObject(name = "Student Deleted", value = StudentExample.DELETE_SUCCESS_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Invalid UUID", value = StudentExample.INVALID_UUID_RESPONSE)))
    @APIResponse(responseCode = "404", description = "Student not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Not Found", value = StudentExample.STUDENT_NOT_FOUND_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = StudentExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response deleteStudent(@PathParam("id") UUID studentId) {
        studentService.deleteStudent(studentId);
        return Response.ok(Map.of("message", "Student successfully deleted!")).build();
    }
}

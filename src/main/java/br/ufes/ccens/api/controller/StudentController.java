package br.ufes.ccens.api.controller;

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
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
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
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    public Response createStudent(@Valid SaveStudentRequest studentRequest) {
        return Response.status(Response.Status.CREATED)
                .entity(studentService.createStudent(studentRequest))
                .build();
    }

    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", description = "Student found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class)))
    @APIResponse(responseCode = "400", description = "Bad Request (Invalid UUID)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @APIResponse(responseCode = "404", description = "Student not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    public Response findStudentById(@PathParam("id") UUID studentId) {
        return Response.ok(studentService.findById(studentId)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @APIResponse(responseCode = "200", description = "Student updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponse.class)))
    @APIResponse(responseCode = "400", description = "Bad Request (Validation Error)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @APIResponse(responseCode = "404", description = "Student not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    public Response updateStudent(
            @PathParam("id") UUID studentId,
            @Valid UpdateStudentRequest studentRequest) {
        return Response.ok(studentService.updateStudent(studentId, studentRequest)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @APIResponse(responseCode = "200", description = "Student deleted", content = @Content(mediaType = "application/json"))
    @APIResponse(responseCode = "404", description = "Student not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    public Response deleteStudent(@PathParam("id") UUID studentId) {
        studentService.deleteStudent(studentId);
        return Response.ok("Student successfully deleted!").build();
    }
}

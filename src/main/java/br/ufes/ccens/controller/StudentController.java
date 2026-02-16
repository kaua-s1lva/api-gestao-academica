package br.ufes.ccens.controller;

import java.time.LocalDate;
import java.util.UUID;

import br.ufes.ccens.dto.request.SaveStudentRequest;
import br.ufes.ccens.service.StudentService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
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
    public Response listAll(
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("pageSize") @DefaultValue("10") Integer pageSize,
            @QueryParam("name") String name,
            @QueryParam("email") String email,
            @QueryParam("registration") String registration,
            @QueryParam("cpf") String cpf,
            @QueryParam("admStart") String admStart,
            @QueryParam("admEnd") String admEnd,
            @QueryParam("birthStart") String birthStart,
            @QueryParam("birthEnd") String birthEnd) {
        
        var students = studentService.listAll(page, pageSize, name, email, registration, cpf, 
            admStart, admEnd, birthStart, birthEnd);
        return Response.ok(students).build();
    }

    @POST
    @Transactional
    @RolesAllowed("User")
    public Response createStudent(SaveStudentRequest studentRequest) {
        return Response.status(Response.Status.CREATED)
                .entity(studentService.createStudent(studentRequest))
                .build();
    }

    @GET
    @Path("/{id}")
    public Response findStudentById(@PathParam("id") UUID studentId) {
        return Response.ok(studentService.findById(studentId)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("User")
    public Response updateStudent(
            @PathParam("id") UUID studentId,
            SaveStudentRequest studentRequest) {
        return Response.ok(studentService.updateStudent(studentId, studentRequest)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("User")
    public Response deleteStudent(@PathParam("id") UUID studentId) {
        studentService.deleteStudent(studentId);
        return Response.ok("Student successfully deleted!").build();
    }
}

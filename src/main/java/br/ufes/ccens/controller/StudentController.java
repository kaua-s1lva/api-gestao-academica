package br.ufes.ccens.controller;

import java.util.UUID;
import java.util.List;

import br.ufes.ccens.entity.StudentEntity;
import br.ufes.ccens.service.StudentService;
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
import jakarta.annotation.security.RolesAllowed;

@Path("/students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("User")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GET
    public Response listAll(
                @QueryParam("page") @DefaultValue("0") Integer page,
                @QueryParam("pageSize") @DefaultValue("100") Integer pageSize
            ) {
        var students = studentService.listAll(page, pageSize);
        return Response.ok(students).build();
    }

    @GET
    @Path("/count")
    @RolesAllowed("User")
    public Response getCount() {
        long total = studentService.getTotalStudents();
        return Response.ok("{\"total\":" + total + "}").build();
    }

    @GET
    @Path("/count/{name}")
    @RolesAllowed("User")
    public Response getCountByName(@PathParam("name") String name) {
        long total = studentService.countStudentsByName(name);
        return Response.ok("{\"name\":\"" + name + "\", \"total\":" + total + "}").build();
    }

    @POST
    @Transactional
    public Response createStudent(StudentEntity studentEntity) {
        return Response.ok(studentService.createStudent(studentEntity)).build();
    }

    @GET
    @Path("/{id}")
    public Response findStudentById(@PathParam("id") UUID studentId) {
        return Response.ok(studentService.findById(studentId)).build();
    }

    @GET
    @Path("/name/{name}")
    @RolesAllowed("User")
    public Response getByPathName(@PathParam("name") String name) {
        List<StudentEntity> students = studentService.findByName(name);
        return Response.ok(students).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateStudent(
                @PathParam("id") UUID studentId, 
                StudentEntity studentEntity
            ) {
        return Response.ok(studentService.updateStudent(studentId, studentEntity)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteStudent(@PathParam("id") UUID studentId) {
        studentService.deleteStudent(studentId);
        return Response.ok("Student successfully deleted!").build();
    }
}

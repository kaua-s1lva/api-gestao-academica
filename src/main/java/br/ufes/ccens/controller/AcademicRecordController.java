package br.ufes.ccens.controller;

import java.util.List;
import java.util.UUID;

import br.ufes.ccens.entity.AcademicRecordEntity;
import br.ufes.ccens.service.AcademicRecordService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/records")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AcademicRecordController {
    @Inject
    AcademicRecordService service;

    @GET
    @RolesAllowed("User")
    public Response getAll(
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("pageSize") @DefaultValue("10") Integer pageSize) {
        List<AcademicRecordEntity> records = service.listAll(page, pageSize);
        return Response.ok(records).build();
    }

    @POST
    @RolesAllowed("User")
    public Response create(AcademicRecordEntity record) {
        service.create(record);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/student/{studentId}")
    @RolesAllowed("User")
    public Response getByStudent(@PathParam("studentId") UUID studentId) {
        List<AcademicRecordEntity> records = service.listByStudent(studentId);
        return Response.ok(records).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("User")
    public Response delete(@PathParam("id") UUID id) {
        service.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/stats/{subjectName}")
    @RolesAllowed("User")
    public Response getStats(@PathParam("subjectName") String subjectName) {
        return Response.ok(service.getSubjectStats(subjectName)).build();
    }

    @GET
    @Path("/search")
    @RolesAllowed("User")
    public Response getHighGrades(@QueryParam("minGrade") Double minGrade) {
        List<AcademicRecordEntity> records = service.listGradesAbove(minGrade);
        return Response.ok(records).build();
    }

    @GET
    @Path("/filter")
    @RolesAllowed("User")
    public Response filterByStudent(@QueryParam("studentName") String studentName) {
        List<AcademicRecordEntity> records = service.listByStudentName(studentName);
        return Response.ok(records).build();
    }
}

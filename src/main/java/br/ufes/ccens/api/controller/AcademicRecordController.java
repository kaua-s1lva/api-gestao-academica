package br.ufes.ccens.api.controller;

import java.util.UUID;
import java.util.Map;

import br.ufes.ccens.api.docs.AcademicRecordExample;
import br.ufes.ccens.api.dto.request.SaveAcademicRecordRequest;
import br.ufes.ccens.api.dto.request.UpdateAcademicRecordRequest;
import br.ufes.ccens.api.dto.response.AcademicRecordResponse;
import br.ufes.ccens.api.dto.response.ApiErrorResponse;
import br.ufes.ccens.core.service.AcademicRecordService;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/academic-records")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class AcademicRecordController {

    private final AcademicRecordService academicRecordService;

    public AcademicRecordController(AcademicRecordService academicRecordService) {
        this.academicRecordService = academicRecordService;
    }

    @GET
    @APIResponse(responseCode = "200", description = "List of academic records", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AcademicRecordResponse.class, type = SchemaType.ARRAY), examples = @ExampleObject(name = "List of Academic Records", value = AcademicRecordExample.LIST_ALL_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = AcademicRecordExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response listAll() {
        return Response.ok(academicRecordService.listAll()).build();
    }

    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", description = "Academic Record found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AcademicRecordResponse.class), examples = @ExampleObject(name = "Academic Record Found", value = AcademicRecordExample.FIND_BY_ID_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request (Invalid UUID)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Invalid UUID", value = AcademicRecordExample.INVALID_UUID_RESPONSE)))
    @APIResponse(responseCode = "404", description = "Academic Record not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Not Found", value = AcademicRecordExample.NOT_FOUND_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = AcademicRecordExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response findById(@PathParam("id") UUID id) {
        return Response.ok(academicRecordService.findById(id)).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    @APIResponse(responseCode = "201", description = "Academic Record created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AcademicRecordResponse.class), examples = @ExampleObject(name = "Academic Record Created", value = AcademicRecordExample.CREATE_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Validation Error", value = AcademicRecordExample.BAD_REQUEST_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = AcademicRecordExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response create(@Valid SaveAcademicRecordRequest request) {
        return Response.status(Response.Status.CREATED)
                .entity(academicRecordService.createAcademicRecord(request))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @APIResponse(responseCode = "200", description = "Academic Record updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AcademicRecordResponse.class), examples = @ExampleObject(name = "Academic Record Updated", value = AcademicRecordExample.UPDATE_SUCCESS_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Validation Error", value = AcademicRecordExample.BAD_REQUEST_RESPONSE)))
    @APIResponse(responseCode = "404", description = "Academic Record not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Not Found", value = AcademicRecordExample.NOT_FOUND_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = AcademicRecordExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response update(@PathParam("id") UUID id, @Valid UpdateAcademicRecordRequest request) {
        return Response.ok(academicRecordService.updateAcademicRecord(id, request)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @APIResponse(responseCode = "200", description = "Academic Record deleted", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Academic Record successfully deleted!\"}"), examples = @ExampleObject(name = "Academic Record Deleted", value = AcademicRecordExample.DELETE_SUCCESS_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Invalid UUID", value = AcademicRecordExample.INVALID_UUID_RESPONSE)))
    @APIResponse(responseCode = "404", description = "Academic Record not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Not Found", value = AcademicRecordExample.NOT_FOUND_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = AcademicRecordExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response delete(@PathParam("id") UUID id) {
        academicRecordService.deleteAcademicRecord(id);
        return Response.ok(Map.of("message", "Academic Record successfully deleted!")).build();
    }
}

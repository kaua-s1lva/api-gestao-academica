package br.ufes.ccens.api.controller;

import java.util.Map;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import br.ufes.ccens.api.docs.DisciplineExample;
import br.ufes.ccens.api.dto.request.SaveDisciplineRequest;
import br.ufes.ccens.api.dto.request.UpdateDisciplineRequest;
import br.ufes.ccens.api.dto.response.ApiErrorResponse;
import br.ufes.ccens.api.dto.response.DisciplineResponse;
import br.ufes.ccens.api.dto.response.PageResponse;
import br.ufes.ccens.core.service.DisciplineService;
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

@Path("/disciplines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class DisciplineController {

    private final DisciplineService disciplineService;

    public DisciplineController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    @GET
    @APIResponse(responseCode = "200", description = "List of disciplines", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class), examples = @ExampleObject(name = "Page of Disciplines", value = DisciplineExample.LIST_ALL_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Invalid Sort", value = DisciplineExample.BAD_REQUEST_RESPONSE)))
    @APIResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Not Found", value = DisciplineExample.NOT_FOUND_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = DisciplineExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response listAll(
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("pageSize") @DefaultValue("10") Integer pageSize,
            @QueryParam("sortBy") @DefaultValue("name") String sortBy,
            @QueryParam("sortDirection") @DefaultValue("asc") String sortDir,
            @QueryParam("name") String name,
            @QueryParam("cod") String cod,
            @QueryParam("ch") String ch,
            @QueryParam("menu") String menu,
            @QueryParam("course") String course) {

        var disciplines = disciplineService.listAll(page, pageSize, sortBy, sortDir, name, cod, ch, menu, course);
        return Response.ok(disciplines).build();
    }

    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", description = "Discipline found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DisciplineResponse.class), examples = @ExampleObject(name = "Discipline Found", value = DisciplineExample.FIND_BY_ID_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Invalid UUID", value = DisciplineExample.INVALID_UUID_RESPONSE)))
    @APIResponse(responseCode = "404", description = "Discipline not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Not Found", value = DisciplineExample.NOT_FOUND_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = DisciplineExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response findById(@PathParam("id") UUID id) {
        return Response.ok(disciplineService.findById(id)).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    @APIResponse(responseCode = "201", description = "Discipline created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DisciplineResponse.class), examples = @ExampleObject(name = "Discipline Created", value = DisciplineExample.CREATE_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Validation Error", value = DisciplineExample.BAD_REQUEST_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = DisciplineExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response create(@Valid SaveDisciplineRequest request) {
        return Response.status(Response.Status.CREATED)
                .entity(disciplineService.createDiscipline(request))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @APIResponse(responseCode = "200", description = "Discipline updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DisciplineResponse.class), examples = @ExampleObject(name = "Discipline Updated", value = DisciplineExample.UPDATE_SUCCESS_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Validation Error", value = DisciplineExample.BAD_REQUEST_RESPONSE)))
    @APIResponse(responseCode = "404", description = "Discipline not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Not Found", value = DisciplineExample.NOT_FOUND_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = DisciplineExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response update(@PathParam("id") UUID id, @Valid UpdateDisciplineRequest request) {
        return Response.ok(disciplineService.updateDiscipline(id, request)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @APIResponse(responseCode = "200", description = "Discipline deleted", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Discipline successfully deleted!\"}"), examples = @ExampleObject(name = "Discipline Deleted", value = DisciplineExample.DELETE_SUCCESS_RESPONSE)))
    @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Invalid UUID", value = DisciplineExample.INVALID_UUID_RESPONSE)))
    @APIResponse(responseCode = "404", description = "Discipline not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Not Found", value = DisciplineExample.NOT_FOUND_RESPONSE)))
    @APIResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Internal Server Error", value = DisciplineExample.INTERNAL_SERVER_ERROR_RESPONSE)))
    public Response delete(@PathParam("id") UUID id) {
        disciplineService.deleteDiscipline(id);
        return Response.ok(Map.of("message", "Discipline successfully deleted!")).build();
    }
}

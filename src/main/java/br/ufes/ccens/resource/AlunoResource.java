package br.ufes.ccens.resource;

import br.ufes.ccens.entity.StudentEntity;
import br.ufes.ccens.service.StudentService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/alunos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlunoResource {
    
    @Inject
    StudentService studentService;

    @GET
    public List<StudentEntity> listarTodos(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("size") @DefaultValue("10") int size) {
        // RF01 - Listagem de todos os registros
        // RF06 - Listagem com paginação
        return studentService.listAll(0, 10); // Usa a lista do service
    }

    @GET
    @Path("/{id}")
    public StudentEntity buscarPorId(@PathParam("id") UUID id) {
        // RF02 - Se não achar, o Mapper que criamos lança o 404 automaticamente
        return studentService.findById(id); 
        // O service já lança a exceção se não achar!
    }

    @POST
    @Transactional
    public Response criar(StudentEntity aluno) {
        // RF03 - Persiste o aluno e retorna 201 Created
        studentService.createStudent(aluno);
        return Response.status(Response.Status.CREATED).entity(aluno).build();
    }


    @PUT
    @Path("/{id}")
    @Transactional
    public StudentEntity atualizar(@PathParam("id") UUID id, StudentEntity alunoAtualizado) {
        // RF04 - Atualizar recurso existente
        return studentService.updateStudent(id, alunoAtualizado); 
        // Usa a lógica pronta do service
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void remover(@PathParam("id") UUID id) {
        // RF05 - Remover recurso usando o serviço que o Kauã criou
        studentService.deleteStudent(id); 
    }
}
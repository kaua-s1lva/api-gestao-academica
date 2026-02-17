package br.ufes.ccens.core.service;

import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;

import br.ufes.ccens.api.dto.request.SaveAcademicRecordRequest;
import br.ufes.ccens.api.dto.request.UpdateAcademicRecordRequest;
import br.ufes.ccens.api.dto.response.AcademicRecordResponse;
import br.ufes.ccens.api.dto.response.PageResponse;
import br.ufes.ccens.api.mapper.AcademicRecordMapper;
import br.ufes.ccens.core.exception.ResourceNotFoundException;
import br.ufes.ccens.core.interceptor.LogTransaction;
import br.ufes.ccens.core.validation.academicrecord.AcademicRecordFilterValidator;
import br.ufes.ccens.core.validation.academicrecord.AcademicRecordValidator;
import br.ufes.ccens.data.entity.AcademicRecordEntity;
import br.ufes.ccens.data.repository.AcademicRecordRepository;
import br.ufes.ccens.data.repository.DisciplineRepository;
import br.ufes.ccens.data.repository.StudentRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@LogTransaction
public class AcademicRecordService {

    private final AcademicRecordRepository academicRecordRepository;
    private final StudentRepository studentRepository;
    private final DisciplineRepository disciplineRepository;
    private final AcademicRecordMapper academicRecordMapper;
    private final AcademicRecordFilterValidator academicRecordFilterValidator;
    private final AcademicRecordValidator academicRecordValidator;
    private static final Logger LOG = Logger.getLogger(AcademicRecordService.class);

    public AcademicRecordService(
            AcademicRecordRepository academicRecordRepository,
            StudentRepository studentRepository,
            DisciplineRepository disciplineRepository,
            AcademicRecordMapper academicRecordMapper,
            AcademicRecordFilterValidator academicRecordFilterValidator,
            AcademicRecordValidator academicRecordValidator) {
        this.academicRecordRepository = academicRecordRepository;
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.academicRecordMapper = academicRecordMapper;
        this.academicRecordFilterValidator = academicRecordFilterValidator;
        this.academicRecordValidator = academicRecordValidator;
    }

    @Transactional
    public AcademicRecordResponse createAcademicRecord(SaveAcademicRecordRequest request) {
        LOG.info("Criando novo registro acadêmico");
        var entity = academicRecordMapper.toEntity(request);

        var student = studentRepository.findByIdOptional(request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudante não encontrado com o ID fornecido."));

        var discipline = disciplineRepository.findByIdOptional(request.disciplineId())
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com o ID fornecido."));

        entity.setStudent(student);
        entity.setDiscipline(discipline);

        academicRecordValidator.validate(entity);

        academicRecordRepository.persist(entity);
        return academicRecordMapper.toResponse(entity);
    }

    public PageResponse<AcademicRecordResponse> listAll(Integer page, Integer pageSize, String sortBy, String sortDir,
            String semester, String status, UUID studentId, UUID disciplineId) {
        LOG.info("Listando todos os registros acadêmicos com filtros");

        academicRecordFilterValidator.validate(page, pageSize, sortBy, sortDir, semester, status, studentId,
                disciplineId);

        if (studentId != null && studentRepository.findByIdOptional(studentId).isEmpty()) {
            throw new ResourceNotFoundException("Estudante não encontrado com o ID fornecido.");
        }

        if (disciplineId != null && disciplineRepository.findByIdOptional(disciplineId).isEmpty()) {
            throw new ResourceNotFoundException("Disciplina não encontrada com o ID fornecido.");
        }

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.Descending
                : Sort.Direction.Ascending;
        Sort sort = Sort.by(sortBy, direction);

        PanacheQuery<AcademicRecordEntity> query;

        if (semester != null || status != null || studentId != null || disciplineId != null) {
            query = academicRecordRepository.findWithFilters(semester, status, studentId, disciplineId, sort);
        } else {
            query = academicRecordRepository.findAll(sort);
        }

        query.page(page, pageSize);

        List<AcademicRecordResponse> content = query.list().stream()
                .map(academicRecordMapper::toResponse)
                .toList();

        if (content.isEmpty()) {
            throw new ResourceNotFoundException("Registros acadêmicos não encontrados.");
        }

        return new PageResponse<>(
                content,
                page,
                pageSize,
                query.count(),
                query.pageCount());
    }

    public AcademicRecordResponse findById(UUID id) {
        LOG.info("Buscando registro acadêmico com ID: " + id);
        return academicRecordRepository.findByIdOptional(id)
                .map(academicRecordMapper::toResponse)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Registro acadêmico não encontrado com o ID fornecido."));
    }

    @Transactional
    public AcademicRecordResponse updateAcademicRecord(UUID id, UpdateAcademicRecordRequest request) {
        LOG.info("Atualizando registro acadêmico ID: " + id);
        var entity = academicRecordRepository.findByIdOptional(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Registro acadêmico não encontrado com o ID fornecido."));

        academicRecordMapper.updateEntityFromDto(request, entity);

        if (request.studentId() != null) {
            var student = studentRepository.findByIdOptional(request.studentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Estudante não encontrado com o ID fornecido."));
            entity.setStudent(student);
        }

        if (request.disciplineId() != null) {
            var discipline = disciplineRepository.findByIdOptional(request.disciplineId())
                    .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com o ID fornecido."));
            entity.setDiscipline(discipline);
        }

        academicRecordValidator.validate(entity);

        academicRecordRepository.persist(entity);
        return academicRecordMapper.toResponse(entity);
    }

    @Transactional
    public void deleteAcademicRecord(UUID id) {
        LOG.info("Removendo registro acadêmico ID: " + id);
        if (!academicRecordRepository.deleteById(id)) {
            throw new ResourceNotFoundException("Registro acadêmico não encontrado com o ID fornecido.");
        }
    }

    public List<AcademicRecordResponse> listByStudent(UUID studentId, UUID disciplineId) {
        LOG.info("Listando registros acadêmicos do aluno ID: " + studentId);

        if (studentRepository.findByIdOptional(studentId).isEmpty()) {
            throw new ResourceNotFoundException("Estudante não encontrado com o ID fornecido.");
        }

        if (disciplineId != null && disciplineRepository.findByIdOptional(disciplineId).isEmpty()) {
            throw new ResourceNotFoundException("Disciplina não encontrada com o ID fornecido.");
        }

        var records = academicRecordRepository.findByStudentAndDiscipline(studentId, disciplineId);

        if (records.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum registro acadêmico encontrado para este aluno.");
        }

        return records.stream()
                .map(academicRecordMapper::toResponse)
                .toList();
    }

    public List<AcademicRecordResponse> listByDiscipline(UUID disciplineId) {
        LOG.info("Listando registros acadêmicos da disciplina ID: " + disciplineId);

        if (disciplineRepository.findByIdOptional(disciplineId).isEmpty()) {
            throw new ResourceNotFoundException("Disciplina não encontrada com o ID fornecido.");
        }

        var records = academicRecordRepository.findByDiscipline(disciplineId);

        if (records.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum registro acadêmico encontrado para esta disciplina.");
        }

        return records.stream()
                .map(academicRecordMapper::toResponse)
                .toList();
    }
}

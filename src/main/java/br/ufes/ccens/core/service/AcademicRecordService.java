package br.ufes.ccens.core.service;

import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;

import br.ufes.ccens.api.dto.request.SaveAcademicRecordRequest;
import br.ufes.ccens.api.dto.request.UpdateAcademicRecordRequest;
import br.ufes.ccens.api.dto.response.AcademicRecordResponse;
import br.ufes.ccens.api.mapper.AcademicRecordMapper;
import br.ufes.ccens.core.exception.ResourceNotFoundException;
import br.ufes.ccens.data.repository.AcademicRecordRepository;
import br.ufes.ccens.data.repository.DisciplineRepository;
import br.ufes.ccens.data.repository.StudentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AcademicRecordService {

    private final AcademicRecordRepository academicRecordRepository;
    private final StudentRepository studentRepository;
    private final DisciplineRepository disciplineRepository;
    private final AcademicRecordMapper academicRecordMapper;
    private static final Logger LOG = Logger.getLogger(AcademicRecordService.class);

    public AcademicRecordService(
            AcademicRecordRepository academicRecordRepository,
            StudentRepository studentRepository,
            DisciplineRepository disciplineRepository,
            AcademicRecordMapper academicRecordMapper) {
        this.academicRecordRepository = academicRecordRepository;
        this.studentRepository = studentRepository;
        this.disciplineRepository = disciplineRepository;
        this.academicRecordMapper = academicRecordMapper;
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

        academicRecordRepository.persist(entity);
        return academicRecordMapper.toResponse(entity);
    }

    public List<AcademicRecordResponse> listAll() {
        LOG.info("Listando todos os registros acadêmicos");
        return academicRecordRepository.listAll().stream()
                .map(academicRecordMapper::toResponse)
                .toList();
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
}

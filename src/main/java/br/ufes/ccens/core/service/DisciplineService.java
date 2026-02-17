package br.ufes.ccens.core.service;

import java.util.List;
import java.util.UUID;

import br.ufes.ccens.api.dto.request.SaveDisciplineRequest;
import br.ufes.ccens.api.dto.request.UpdateDisciplineRequest;
import br.ufes.ccens.api.dto.response.DisciplineResponse;
import br.ufes.ccens.api.dto.response.PageResponse;
import br.ufes.ccens.api.mapper.DisciplineMapper;
import br.ufes.ccens.core.exception.ResourceNotFoundException;
import br.ufes.ccens.core.interceptor.LogTransaction;
import br.ufes.ccens.core.validation.discipline.DisciplineFilterValidator;
import br.ufes.ccens.data.entity.DisciplineEntity;
import br.ufes.ccens.data.repository.DisciplineRepository;
import br.ufes.ccens.core.exception.DuplicateResourceException;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@LogTransaction
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;
    private final DisciplineMapper disciplineMapper;
    private final DisciplineFilterValidator disciplineFilterValidator;

    public DisciplineService(DisciplineRepository disciplineRepository, DisciplineMapper disciplineMapper,
            DisciplineFilterValidator disciplineFilterValidator) {
        this.disciplineRepository = disciplineRepository;
        this.disciplineMapper = disciplineMapper;
        this.disciplineFilterValidator = disciplineFilterValidator;
    }

    @Transactional
    public DisciplineResponse createDiscipline(SaveDisciplineRequest request) {

        if (disciplineRepository.find("cod", request.cod()).count() > 0) {
            throw new DuplicateResourceException("cod", "Código de disciplina já cadastrado.");
        }

        var disciplineEntity = disciplineMapper.toEntity(request);
        disciplineRepository.persist(disciplineEntity);
        return disciplineMapper.toResponse(disciplineEntity);
    }

    @Transactional
    public PageResponse<DisciplineResponse> listAll(Integer page, Integer pageSize, String sortBy, String sortDir,
            String name, String cod, String ch, String menu, String course) {

        disciplineFilterValidator.validate(page, pageSize, sortBy, sortDir, cod, ch);

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.Descending
                : Sort.Direction.Ascending;
        Sort sort = Sort.by(sortBy, direction);

        PanacheQuery<DisciplineEntity> query;

        if (name != null || cod != null || ch != null || menu != null || course != null) {
            query = disciplineRepository.findWithFilters(name, cod, ch, menu, course, sort);
        } else {
            query = disciplineRepository.findAll(sort);
        }

        query.page(page, pageSize);

        List<DisciplineResponse> content = query.list().stream()
                .map(disciplineMapper::toResponse)
                .toList();

        if (content.isEmpty()) {
            throw new ResourceNotFoundException("Disciplines not found.");
        }

        return new PageResponse<>(
                content,
                page,
                pageSize,
                query.count(),
                query.pageCount());
    }

    public DisciplineResponse findById(UUID id) {
        return disciplineRepository.findByIdOptional(id)
                .map(disciplineMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com o ID fornecido."));
    }

    @Transactional
    public DisciplineResponse updateDiscipline(UUID id, UpdateDisciplineRequest request) {
        var entity = disciplineRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com o ID fornecido."));

        if (!entity.getCod().equals(request.cod())) {
            if (disciplineRepository.find("cod", request.cod()).count() > 0) {
                throw new DuplicateResourceException("cod", "Código de disciplina já cadastrado.");
            }
        }

        disciplineMapper.updateEntityFromDto(request, entity);
        disciplineRepository.persist(entity);
        return disciplineMapper.toResponse(entity);
    }

    @Transactional
    public void deleteDiscipline(UUID id) {
        if (!disciplineRepository.deleteById(id)) {
            throw new ResourceNotFoundException("Disciplina não encontrada com o ID fornecido.");
        }
    }
}

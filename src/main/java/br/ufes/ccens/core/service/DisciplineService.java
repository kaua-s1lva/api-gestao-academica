package br.ufes.ccens.core.service;

import java.util.List;
import java.util.UUID;

import br.ufes.ccens.api.dto.request.SaveDisciplineRequest;
import br.ufes.ccens.api.dto.request.UpdateDisciplineRequest;
import br.ufes.ccens.api.dto.response.DisciplineResponse;
import br.ufes.ccens.api.mapper.DisciplineMapper;
import br.ufes.ccens.core.exception.ResourceNotFoundException;
import br.ufes.ccens.core.interceptor.LogTransaction;
import br.ufes.ccens.data.repository.DisciplineRepository;
import br.ufes.ccens.core.exception.DuplicateResourceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@LogTransaction
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;
    private final DisciplineMapper disciplineMapper;

    public DisciplineService(DisciplineRepository disciplineRepository, DisciplineMapper disciplineMapper) {
        this.disciplineRepository = disciplineRepository;
        this.disciplineMapper = disciplineMapper;
    }

    @Transactional
    public DisciplineResponse createDiscipline(SaveDisciplineRequest request) {

        if (disciplineRepository.find("cod", request.cod()).count() > 0) {
            throw new DuplicateResourceException("cod", "Código de disciplina já cadastrado.");
        }

        var entity = disciplineMapper.toEntity(request);
        disciplineRepository.persist(entity);
        return disciplineMapper.toResponse(entity);
    }

    public List<DisciplineResponse> listAll() {
        return disciplineRepository.listAll().stream()
                .map(disciplineMapper::toResponse)
                .toList();
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

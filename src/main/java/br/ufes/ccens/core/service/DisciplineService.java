package br.ufes.ccens.core.service;

import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;

import br.ufes.ccens.api.dto.request.SaveDisciplineRequest;
import br.ufes.ccens.api.dto.request.UpdateDisciplineRequest;
import br.ufes.ccens.api.dto.response.DisciplineResponse;
import br.ufes.ccens.api.mapper.DisciplineMapper;
import br.ufes.ccens.core.exception.ResourceNotFoundException;
import br.ufes.ccens.core.interceptor.LogTransaction;
import br.ufes.ccens.data.repository.DisciplineRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@LogTransaction
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;
    private final DisciplineMapper disciplineMapper;
    private static final Logger LOG = Logger.getLogger(DisciplineService.class);

    public DisciplineService(DisciplineRepository disciplineRepository, DisciplineMapper disciplineMapper) {
        this.disciplineRepository = disciplineRepository;
        this.disciplineMapper = disciplineMapper;
    }

    @Transactional
    public DisciplineResponse createDiscipline(SaveDisciplineRequest request) {
        LOG.info("Criando nova disciplina: " + request.name());
        var entity = disciplineMapper.toEntity(request);
        disciplineRepository.persist(entity);
        return disciplineMapper.toResponse(entity);
    }

    public List<DisciplineResponse> listAll() {
        LOG.info("Listando todas as disciplinas");
        return disciplineRepository.listAll().stream()
                .map(disciplineMapper::toResponse)
                .toList();
    }

    public DisciplineResponse findById(UUID id) {
        LOG.info("Buscando disciplina com ID: " + id);
        return disciplineRepository.findByIdOptional(id)
                .map(disciplineMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com o ID fornecido."));
    }

    @Transactional
    public DisciplineResponse updateDiscipline(UUID id, UpdateDisciplineRequest request) {
        LOG.info("Atualizando disciplina ID: " + id);
        var entity = disciplineRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada com o ID fornecido."));

        disciplineMapper.updateEntityFromDto(request, entity);
        disciplineRepository.persist(entity);
        return disciplineMapper.toResponse(entity);
    }

    @Transactional
    public void deleteDiscipline(UUID id) {
        LOG.info("Removendo disciplina ID: " + id);
        if (!disciplineRepository.deleteById(id)) {
            throw new ResourceNotFoundException("Disciplina não encontrada com o ID fornecido.");
        }
    }
}

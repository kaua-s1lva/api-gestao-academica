package br.ufes.ccens.api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import br.ufes.ccens.api.dto.request.SaveDisciplineRequest;
import br.ufes.ccens.api.dto.request.UpdateDisciplineRequest;
import br.ufes.ccens.api.dto.response.DisciplineResponse;
import br.ufes.ccens.data.entity.DisciplineEntity;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DisciplineMapper {
    DisciplineEntity toEntity(SaveDisciplineRequest request);

    DisciplineEntity toEntity(UpdateDisciplineRequest request);

    DisciplineResponse toResponse(DisciplineEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UpdateDisciplineRequest request, @MappingTarget DisciplineEntity entity);
}

package br.ufes.ccens.api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import br.ufes.ccens.api.dto.request.SaveAcademicRecordRequest;
import br.ufes.ccens.api.dto.request.UpdateAcademicRecordRequest;
import br.ufes.ccens.api.dto.response.AcademicRecordResponse;
import br.ufes.ccens.data.entity.AcademicRecordEntity;

@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { StudentMapper.class,
        DisciplineMapper.class })
public interface AcademicRecordMapper {
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "discipline", ignore = true)
    AcademicRecordEntity toEntity(SaveAcademicRecordRequest request);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "discipline", ignore = true)
    AcademicRecordEntity toEntity(UpdateAcademicRecordRequest request);

    AcademicRecordResponse toResponse(AcademicRecordEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "discipline", ignore = true)
    void updateEntityFromDto(UpdateAcademicRecordRequest request, @MappingTarget AcademicRecordEntity entity);
}

package br.ufes.ccens.api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import br.ufes.ccens.api.dto.request.SaveStudentRequest;
import br.ufes.ccens.api.dto.request.UpdateStudentRequest;
import br.ufes.ccens.api.dto.response.StudentResponse;
import br.ufes.ccens.data.entity.StudentEntity;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE) 
public interface StudentMapper {
    StudentEntity toEntity(SaveStudentRequest request);
    StudentEntity toEntity(UpdateStudentRequest request);
    StudentResponse toResponse(StudentEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UpdateStudentRequest request, @MappingTarget StudentEntity entity);
}
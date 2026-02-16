package br.ufes.ccens.api.mapper;

import br.ufes.ccens.api.dto.request.SaveStudentRequest;
import br.ufes.ccens.api.dto.response.StudentResponse;
import br.ufes.ccens.data.entity.StudentEntity;

import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi") 
public interface StudentMapper {
    StudentEntity toEntity(SaveStudentRequest request);
    StudentResponse toResponse(StudentEntity entity);
}
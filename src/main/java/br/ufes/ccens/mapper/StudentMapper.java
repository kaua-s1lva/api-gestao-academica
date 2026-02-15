package br.ufes.ccens.mapper;

import br.ufes.ccens.dto.request.SaveStudentRequest;
import br.ufes.ccens.entity.StudentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi") 
public interface StudentMapper {
    StudentEntity toEntity(SaveStudentRequest request);
}
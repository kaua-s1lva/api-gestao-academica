package br.ufes.ccens.api.mapper;

import org.mapstruct.Mapper;

import br.ufes.ccens.api.dto.request.RegisterUserRequest;
import br.ufes.ccens.data.entity.UserEntity;

@Mapper(componentModel = "cdi") 
public interface UserMapper {
    UserEntity toEntity(RegisterUserRequest request);
}

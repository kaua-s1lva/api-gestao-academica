package br.ufes.ccens.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import br.ufes.ccens.api.dto.request.RegisterUserRequest;
import br.ufes.ccens.api.dto.response.RegisterUserResponse;
import br.ufes.ccens.data.entity.UserEntity;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE) 
public interface UserMapper {
    UserEntity toEntity(RegisterUserRequest request);
    RegisterUserResponse toResponse(UserEntity entity);
}

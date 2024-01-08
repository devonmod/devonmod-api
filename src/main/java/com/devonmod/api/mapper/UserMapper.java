package com.devonmod.api.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import com.devonmod.api.dto.request.UserCreateRequestDto;
import com.devonmod.api.dto.response.UserResponseDto;
import com.devonmod.api.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface UserMapper {

  UserResponseDto mapToDto(UserEntity userEntity);

  UserEntity mapToEntity(UserCreateRequestDto userCreateRequestDto);

}

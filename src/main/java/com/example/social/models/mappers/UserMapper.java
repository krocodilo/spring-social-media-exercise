package com.example.social.models.mappers;

import com.example.social.models.jpa.UserEntity;
import com.example.social.models.requests.NewUserReqDTO;
import com.example.social.models.responseDTOs.UserRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    UserRespDTO toUserDTO(UserEntity userEntity);

    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "credentials", ignore = true)
    UserEntity toUserEntity(NewUserReqDTO newUserReqDTO);
}

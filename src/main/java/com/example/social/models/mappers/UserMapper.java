package com.example.social.models.mappers;

import com.example.social.models.jpa.UserEntity;
import com.example.social.models.requestDTOs.NewUserReqDTO;
import com.example.social.models.responseDTOs.UserRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "birthDate", target = "birth_date")
    UserRespDTO toUserRespDTO(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "credentials", ignore = true)
    @Mapping(source = "birth_date", target = "birthDate")
    UserEntity toUserEntity(NewUserReqDTO newUserReqDTO);
}

package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.AwardDTO;
import com.portfolio.backend.entity.Award;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AwardMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    AwardDTO toDto(Award award);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // User will be set by the service
    Award toEntity(AwardDTO awardDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // User should not be updated via DTO
    void updateEntityFromDto(AwardDTO awardDTO, @MappingTarget Award award);
}
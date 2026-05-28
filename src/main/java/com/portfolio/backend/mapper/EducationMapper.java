package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.EducationDTO;
import com.portfolio.backend.entity.Education;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {EducationAchievementMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EducationMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(target = "achievements", source = "achievements")
    EducationDTO toDto(Education education);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "achievements", ignore = true)
    Education toEntity(EducationDTO educationDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "achievements", ignore = true)
    void updateEntityFromDto(EducationDTO educationDTO, @MappingTarget Education education);
}
package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.ExperienceDTO;
import com.portfolio.backend.entity.Experience;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {ExperienceHighlightMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExperienceMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(target = "highlights", source = "highlights")
    ExperienceDTO toDto(Experience experience);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "highlights", ignore = true)
    Experience toEntity(ExperienceDTO experienceDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "highlights", ignore = true)
    void updateEntityFromDto(ExperienceDTO experienceDTO, @MappingTarget Experience experience);
}
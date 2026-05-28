package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.ExperienceHighlightDTO;
import com.portfolio.backend.entity.ExperienceHighlight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExperienceHighlightMapper {
    @Mapping(source = "experience.id", target = "experienceId")
    ExperienceHighlightDTO toDto(ExperienceHighlight experienceHighlight);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "experience", ignore = true)
    ExperienceHighlight toEntity(ExperienceHighlightDTO experienceHighlightDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "experience", ignore = true)
    void updateEntityFromDto(ExperienceHighlightDTO experienceHighlightDTO, @MappingTarget ExperienceHighlight experienceHighlight);
}
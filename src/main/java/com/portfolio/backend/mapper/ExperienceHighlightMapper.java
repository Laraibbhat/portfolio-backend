package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.ExperienceHighlightDTO;
import com.portfolio.backend.entity.ExperienceHighlight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExperienceHighlightMapper {

    ExperienceHighlightDTO toDto(ExperienceHighlight experienceHighlight);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "experience", ignore = true) // Experience will be set by the service
    ExperienceHighlight toEntity(ExperienceHighlightDTO experienceHighlightDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "experience", ignore = true) // Experience should not be updated via highlight DTO
    void updateEntityFromDto(ExperienceHighlightDTO experienceHighlightDTO, @MappingTarget ExperienceHighlight experienceHighlight);
}
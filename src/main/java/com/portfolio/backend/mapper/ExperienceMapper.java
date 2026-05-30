package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.ExperienceDTO;
import com.portfolio.backend.dto.ExperienceHighlightDTO;
import com.portfolio.backend.entity.Experience;
import com.portfolio.backend.entity.ExperienceHighlight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {ExperienceHighlightMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExperienceMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    ExperienceDTO toDto(Experience experience);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // User will be set by the service
    @Mapping(target = "highlights", ignore = true) // Highlights will be handled separately or by service
    Experience toEntity(ExperienceDTO experienceDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // User should not be updated via experience DTO
    @Mapping(target = "highlights", ignore = true) // Highlights will be handled separately or by service
    void updateEntityFromDto(ExperienceDTO experienceDTO, @MappingTarget Experience experience);

    // Helper method to map highlights from DTO to Entity, setting the parent experience
    default Set<ExperienceHighlight> mapHighlights(Set<ExperienceHighlightDTO> highlightDTOS, Experience experience, ExperienceHighlightMapper highlightMapper) {
        if (highlightDTOS == null) {
            return null;
        }
        return highlightDTOS.stream()
                .map(dto -> {
                    ExperienceHighlight highlight = highlightMapper.toEntity(dto);
                    highlight.setExperience(experience);
                    return highlight;
                })
                .collect(Collectors.toSet());
    }
}
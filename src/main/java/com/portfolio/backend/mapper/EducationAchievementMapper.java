package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.EducationAchievementDTO;
import com.portfolio.backend.entity.EducationAchievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EducationAchievementMapper {
    @Mapping(source = "education.id", target = "educationId")
    EducationAchievementDTO toDto(EducationAchievement educationAchievement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "education", ignore = true)
    EducationAchievement toEntity(EducationAchievementDTO educationAchievementDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "education", ignore = true)
    void updateEntityFromDto(EducationAchievementDTO educationAchievementDTO, @MappingTarget EducationAchievement educationAchievement);
}
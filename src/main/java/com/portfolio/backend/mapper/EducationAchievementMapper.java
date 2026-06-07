package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.EducationAchievementDTO;
import com.portfolio.backend.entity.EducationAchievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EducationAchievementMapper {

    EducationAchievementDTO toDto(EducationAchievement educationAchievement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "education", ignore = true) // Education will be set by the service
    EducationAchievement toEntity(EducationAchievementDTO educationAchievementDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "education", ignore = true) // Education should not be updated via achievement DTO
    void updateEntityFromDto(EducationAchievementDTO educationAchievementDTO, @MappingTarget EducationAchievement educationAchievement);
}
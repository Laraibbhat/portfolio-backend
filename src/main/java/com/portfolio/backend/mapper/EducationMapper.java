package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.EducationAchievementDTO;
import com.portfolio.backend.dto.EducationDTO;
import com.portfolio.backend.entity.Education;
import com.portfolio.backend.entity.EducationAchievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {EducationAchievementMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EducationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    EducationDTO toDto(Education education);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // User will be set by the service
    @Mapping(target = "achievements", ignore = true) // Achievements will be handled separately or by service
    Education toEntity(EducationDTO educationDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // User should not be updated via education DTO
    @Mapping(target = "achievements", ignore = true) // Achievements will be handled separately or by service
    void updateEntityFromDto(EducationDTO educationDTO, @MappingTarget Education education);

    // Helper method to map achievements from DTO to Entity, setting the parent education
    default Set<EducationAchievement> mapAchievements(Set<EducationAchievementDTO> achievementDTOS, Education education, EducationAchievementMapper achievementMapper) {
        if (achievementDTOS == null) {
            return null;
        }
        return achievementDTOS.stream()
                .map(dto -> {
                    EducationAchievement achievement = achievementMapper.toEntity(dto);
                    achievement.setEducation(education);
                    return achievement;
                })
                .collect(Collectors.toSet());
    }
}
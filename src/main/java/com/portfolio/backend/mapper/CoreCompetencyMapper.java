package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.CoreCompetencyDTO;
import com.portfolio.backend.entity.CoreCompetency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CoreCompetencyMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    CoreCompetencyDTO toDto(CoreCompetency coreCompetency);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // User will be set by the service
    CoreCompetency toEntity(CoreCompetencyDTO coreCompetencyDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // User should not be updated via DTO
    void updateEntityFromDto(CoreCompetencyDTO coreCompetencyDTO, @MappingTarget CoreCompetency coreCompetency);
}
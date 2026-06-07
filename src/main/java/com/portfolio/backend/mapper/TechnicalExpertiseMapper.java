package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.TechnicalExpertiseDTO;
import com.portfolio.backend.entity.TechnicalExpertise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TechnicalExpertiseMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    TechnicalExpertiseDTO toDto(TechnicalExpertise technicalExpertise);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // User will be set by the service
    TechnicalExpertise toEntity(TechnicalExpertiseDTO technicalExpertiseDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // User should not be updated via DTO
    void updateEntityFromDto(TechnicalExpertiseDTO technicalExpertiseDTO, @MappingTarget TechnicalExpertise technicalExpertise);
}
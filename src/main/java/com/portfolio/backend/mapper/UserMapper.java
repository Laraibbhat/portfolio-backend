package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.UserDTO;
import com.portfolio.backend.dto.UserRequestDTO;
import com.portfolio.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        uses = {TechnicalExpertiseMapper.class, ExperienceMapper.class, EducationMapper.class,
                CertificationMapper.class, PublicationMapper.class, AwardMapper.class, CoreCompetencyMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "technicalExpertise", source = "technicalExpertise")
    @Mapping(target = "experiences", source = "experiences")
    @Mapping(target = "education", source = "education")
    @Mapping(target = "certifications", source = "certifications")
    @Mapping(target = "publications", source = "publications")
    @Mapping(target = "awards", source = "awards")
    @Mapping(target = "coreCompetencies", source = "coreCompetencies")
    UserDTO toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "technicalExpertise", ignore = true)
    @Mapping(target = "experiences", ignore = true)
    @Mapping(target = "education", ignore = true)
    @Mapping(target = "certifications", ignore = true)
    @Mapping(target = "publications", ignore = true)
    @Mapping(target = "awards", ignore = true)
    @Mapping(target = "coreCompetencies", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "technicalExpertise", ignore = true)
    @Mapping(target = "experiences", ignore = true)
    @Mapping(target = "education", ignore = true)
    @Mapping(target = "certifications", ignore = true)
    @Mapping(target = "publications", ignore = true)
    @Mapping(target = "awards", ignore = true)
    @Mapping(target = "coreCompetencies", ignore = true)
    void updateEntityFromDto(UserRequestDTO userRequestDTO, @MappingTarget User user);
}
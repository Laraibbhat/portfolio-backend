package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.*;
import com.portfolio.backend.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named; // Explicitly import Named
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {TechnicalExpertiseMapper.class, ExperienceMapper.class, EducationMapper.class,
                CertificationMapper.class, PublicationMapper.class, CoreCompetencyMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfileMapper {

    @Mapping(target = "experienceYears", source = "experienceYears", qualifiedByName = "bigDecimalToDouble")
    @Mapping(target = "yearsAsSenior", source = "yearsAsSenior", qualifiedByName = "bigDecimalToDouble")
    @Mapping(target = "technicalExpertise", expression = "java(mapTechnicalExpertise(user.getTechnicalExpertise()))")
    @Mapping(target = "experiences", source = "experiences")
    @Mapping(target = "educations", source = "education") // Note: DTO uses 'educations', Entity uses 'education'
    @Mapping(target = "certifications", source = "certifications")
    @Mapping(target = "publications", source = "publications")
    @Mapping(target = "awards", expression = "java(mapAwards(user.getAwards()))")
    @Mapping(target = "coreCompetencies", source = "coreCompetencies")
    ProfileDTO toProfileDTO(User user);

    // Mapping from ProfileDTO to User entity (for creation/update)
    // Note: Collections (technicalExpertise, experiences, etc.) are ignored here
    // and will be handled separately in the service layer to manage relationships.
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
    @Mapping(target = "experienceYears", source = "experienceYears", qualifiedByName = "doubleToBigDecimal")
    @Mapping(target = "yearsAsSenior", source = "yearsAsSenior", qualifiedByName = "doubleToBigDecimal")
    User toUser(ProfileDTO profileDTO);

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
    @Mapping(target = "experienceYears", source = "experienceYears", qualifiedByName = "doubleToBigDecimal")
    @Mapping(target = "yearsAsSenior", source = "yearsAsSenior", qualifiedByName = "doubleToBigDecimal")
    void updateUserFromProfileDTO(ProfileDTO profileDTO, @MappingTarget User user);

    // Custom mapping for TechnicalExpertise Set to Map<String, List<String>>
    default Map<String, List<String>> mapTechnicalExpertise(Set<TechnicalExpertise> technicalExpertiseSet) {
        if (technicalExpertiseSet == null) {
            return null;
        }
        return technicalExpertiseSet.stream()
                .collect(Collectors.groupingBy(
                        TechnicalExpertise::getCategory,
                        Collectors.mapping(TechnicalExpertise::getSkill, Collectors.toList())
                ));
    }

    // Custom mapping for Awards Set to List<String>
    default List<String> mapAwards(Set<Award> awardSet) {
        if (awardSet == null) {
            return null;
        }
        return awardSet.stream()
                .map(Award::getAwardText)
                .collect(Collectors.toList());
    }

    // Qualified methods for BigDecimal to Double and vice-versa
    @Named("bigDecimalToDouble")
    default Double bigDecimalToDouble(BigDecimal bigDecimal) {
        return bigDecimal == null ? null : bigDecimal.doubleValue();
    }

    @Named("doubleToBigDecimal")
    default BigDecimal doubleToBigDecimal(Double aDouble) {
        return aDouble == null ? null : BigDecimal.valueOf(aDouble);
    }
}
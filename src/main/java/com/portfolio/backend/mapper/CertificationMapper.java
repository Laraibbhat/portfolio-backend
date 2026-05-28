package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.CertificationDTO;
import com.portfolio.backend.entity.Certification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CertificationMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    CertificationDTO toDto(Certification certification);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Certification toEntity(CertificationDTO certificationDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(CertificationDTO certificationDTO, @MappingTarget Certification certification);
}
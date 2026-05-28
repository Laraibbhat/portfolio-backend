package com.portfolio.backend.mapper;

import com.portfolio.backend.dto.PublicationDTO;
import com.portfolio.backend.entity.Publication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PublicationMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    PublicationDTO toDto(Publication publication);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Publication toEntity(PublicationDTO publicationDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(PublicationDTO publicationDTO, @MappingTarget Publication publication);
}
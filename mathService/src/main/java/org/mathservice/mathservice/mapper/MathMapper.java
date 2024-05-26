package org.mathservice.mathservice.mapper;

import org.mapstruct.Mapper;
import org.mathservice.mathservice.dto.MathDTO;
import org.mathservice.mathservice.entities.MathEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MathMapper {
    MathDTO fromEntityToDTO(MathEntity entity);
    MathEntity fromDTOToEntity(MathDTO dto);
    List<MathDTO> fromEntityListToDTOList(List<MathEntity> entities);
    List<MathDTO> fromEntityIterableToDTOList(Iterable<MathEntity> entities);
    List<MathEntity> fromDTOListToEntityList(List<MathDTO> dtos);
}

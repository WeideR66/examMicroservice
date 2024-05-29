package org.historyservice.historyservice.mapper;

import org.historyservice.historyservice.dto.HistoryDTO;
import org.historyservice.historyservice.entities.HistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HistoryMapper {
    @Mapping(target = "id", source = "historyEntity.id")
    HistoryDTO fromEntityToDTO(HistoryEntity historyEntity);
    HistoryEntity fromDTOToEntity(HistoryDTO historyDTO);
    List<HistoryDTO> fromEntityListToDTOList(List<HistoryEntity> historyEntities);
    List<HistoryDTO> fromEntityIterableToDTOList(Iterable<HistoryEntity> historyEntities);
    List<HistoryEntity> fromDTOListToEntityList(List<HistoryDTO> historyDTOs);
}

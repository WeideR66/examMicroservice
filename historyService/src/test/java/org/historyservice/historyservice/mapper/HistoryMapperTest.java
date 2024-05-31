package org.historyservice.historyservice.mapper;

import org.historyservice.historyservice.dto.HistoryDTO;
import org.historyservice.historyservice.entities.HistoryEntity;
import org.historyservice.historyservice.utils.QuestionLevel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka
class HistoryMapperTest {
    @Autowired
    private HistoryMapper mapper;

    @Test
    public void rightTransformFromEntityToDTO() {
        HistoryEntity entity = new HistoryEntity(null, "question", "answer", QuestionLevel.medium);
        System.out.println(entity);
        HistoryDTO dto = mapper.fromEntityToDTO(entity);
        System.out.println(dto);
        assertAll(
                () -> assertEquals(entity.getQuestion(), dto.getQuestion()),
                () -> assertEquals(entity.getAnswer(), dto.getAnswer()),
                () -> assertEquals(entity.getLevel(), dto.getLevel())
        );
    }

    @Test
    public void rightTransformFromDTOToEntity() {
        HistoryDTO dto = new HistoryDTO(null, "question", "answer", QuestionLevel.medium);
        System.out.println(dto);
        HistoryEntity entity = mapper.fromDTOToEntity(dto);
        System.out.println(entity);
        assertAll(
                () -> assertEquals(entity.getQuestion(), dto.getQuestion()),
                () -> assertEquals(entity.getAnswer(), dto.getAnswer()),
                () -> assertEquals(entity.getLevel(), dto.getLevel())
        );
    }

    @Test
    public void rightTransformFromEntityListToDtoList() {
        List<HistoryEntity> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entities.add(new HistoryEntity(null, "Questions " + i, "Answer " + i, QuestionLevel.easy));
        }

        List<HistoryDTO> dtos = mapper.fromEntityListToDTOList(entities);

        System.out.println(entities);
        System.out.println(dtos);
        assertEquals(entities.size(), dtos.size());
    }

    @Test
    public void rightTransformFromDtoListToEntityList() {
        List<HistoryDTO> dtos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dtos.add(new HistoryDTO(null, "Questions " + i, "Answer " + i, QuestionLevel.easy));
        }

        List<HistoryEntity> entities = mapper.fromDTOListToEntityList(dtos);

        assertEquals(dtos.size(), entities.size());
    }
}
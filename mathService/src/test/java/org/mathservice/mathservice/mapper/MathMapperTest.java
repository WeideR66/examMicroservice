package org.mathservice.mathservice.mapper;

import org.junit.jupiter.api.Test;
import org.mathservice.mathservice.dto.MathDTO;
import org.mathservice.mathservice.entities.MathEntity;
import org.mathservice.mathservice.utils.QuestionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka
class MathMapperTest {
    @Autowired
    private MathMapper mapper;

    @Test
    public void rightTransformFromEntityToDTO() {
        MathEntity entity = new MathEntity(null, "question", "answer", QuestionLevel.medium);
        System.out.println(entity);
        MathDTO dto = mapper.fromEntityToDTO(entity);
        System.out.println(dto);
        assertAll(
                () -> assertEquals(entity.getQuestion(), dto.getQuestion()),
                () -> assertEquals(entity.getAnswer(), dto.getAnswer()),
                () -> assertEquals(entity.getLevel(), dto.getLevel())
        );
    }

    @Test
    public void rightTransformFromDTOToEntity() {
        MathDTO dto = new MathDTO(null, "question", "answer", QuestionLevel.medium);
        System.out.println(dto);
        MathEntity entity = mapper.fromDTOToEntity(dto);
        System.out.println(entity);
        assertAll(
                () -> assertEquals(entity.getQuestion(), dto.getQuestion()),
                () -> assertEquals(entity.getAnswer(), dto.getAnswer()),
                () -> assertEquals(entity.getLevel(), dto.getLevel())
        );
    }

    @Test
    public void rightTransformFromEntityListToDtoList() {
        List<MathEntity> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entities.add(new MathEntity(null, "Questions " + i, "Answer " + i, QuestionLevel.easy));
        }

        List<MathDTO> dtos = mapper.fromEntityListToDTOList(entities);

        System.out.println(entities);
        System.out.println(dtos);
        assertEquals(entities.size(), dtos.size());
    }

    @Test
    public void rightTransformFromDtoListToEntityList() {
        List<MathDTO> dtos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dtos.add(new MathDTO(null, "Questions " + i, "Answer " + i, QuestionLevel.easy));
        }

        List<MathEntity> entities = mapper.fromDTOListToEntityList(dtos);

        assertEquals(dtos.size(), entities.size());
    }
}
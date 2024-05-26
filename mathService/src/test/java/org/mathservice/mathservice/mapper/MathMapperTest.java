package org.mathservice.mathservice.mapper;

import org.junit.jupiter.api.Test;
import org.mathservice.mathservice.dto.MathDTO;
import org.mathservice.mathservice.entities.MathEntity;
import org.mathservice.mathservice.utils.QuestionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
        MathDTO dto = new MathDTO("question", "answer", QuestionLevel.medium);
        System.out.println(dto);
        MathEntity entity = mapper.fromDTOToEntity(dto);
        System.out.println(entity);
        assertAll(
                () -> assertEquals(entity.getQuestion(), dto.getQuestion()),
                () -> assertEquals(entity.getAnswer(), dto.getAnswer()),
                () -> assertEquals(entity.getLevel(), dto.getLevel())
        );
    }
}
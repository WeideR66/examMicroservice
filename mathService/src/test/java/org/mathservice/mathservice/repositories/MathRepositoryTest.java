package org.mathservice.mathservice.repositories;

import org.junit.jupiter.api.Test;
import org.mathservice.mathservice.entities.MathEntity;
import org.mathservice.mathservice.utils.QuestionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MathRepositoryTest {

    @Autowired
    private MathRepository mathRepository;

    private MathEntity createMathEntity(String question, String answer, QuestionLevel level) {
        MathEntity entity = new MathEntity();
        entity.setQuestion(question);
        entity.setAnswer(answer);
        entity.setLevel(level);
        return entity;
    }

    @Test
    public void whenAddEntityThenEntitySavesCorrectly() {
        MathEntity outerEntity = createMathEntity("question", "answer", QuestionLevel.easy);
        mathRepository.save(outerEntity);

        MathEntity innerEntity = mathRepository.findById(outerEntity.getId()).get();

        assertEquals(outerEntity, innerEntity);
    }

    @Test
    public void whenAddEntitiesThenWheyAllSaves() {
        List<MathEntity> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entities.add(createMathEntity("question" + i, "answer" + i, QuestionLevel.easy));
        }
        mathRepository.saveAll(entities);

        assertEquals(10, mathRepository.count());
    }

    @Test
    public void repositoryCanReturnRightIds() {
        List<MathEntity> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entities.add(createMathEntity("question" + i, "answer" + i, QuestionLevel.easy));
        }
        mathRepository.saveAll(entities);

        List<Long> expectedIds = entities.stream().map(MathEntity::getId).toList();

        List<Long> actualIds = mathRepository.findAllId();

        assertEquals(expectedIds, actualIds);
    }

    @Test
    public void repositoryCanReturnRightIdsByLevel() {
        List<MathEntity> entities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            entities.add(createMathEntity("question" + i, "answer" + i, QuestionLevel.medium));
        }
        mathRepository.saveAll(entities);

        List<Long> expectedIds = entities.stream().map(MathEntity::getId).toList();

        List<Long> actualIds = mathRepository.findIdsByLevel(QuestionLevel.medium);

        assertEquals(expectedIds, actualIds);
        assertEquals(expectedIds.size(), actualIds.size());
    }

    @Test
    public void repositoryCanReturnRightEntitiesByLevel() {
        List<MathEntity> easyEntities = new ArrayList<>();
        List<MathEntity> mediumEntities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            easyEntities.add(createMathEntity("question" + i, "answer" + i, QuestionLevel.easy));
        }
        for (int i = 5; i < 15; i++) {
            mediumEntities.add(createMathEntity("question" + i, "answer" + i, QuestionLevel.medium));
        }
        mathRepository.saveAll(easyEntities);
        mathRepository.saveAll(mediumEntities);

        List<MathEntity> actualEntities = mathRepository.findAllByLevel(QuestionLevel.medium);

        assertEquals(mediumEntities, actualEntities);
        assertEquals(mediumEntities.size(), actualEntities.size());
    }
}
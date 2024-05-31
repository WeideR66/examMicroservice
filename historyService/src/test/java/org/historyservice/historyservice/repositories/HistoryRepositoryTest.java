package org.historyservice.historyservice.repositories;

import org.historyservice.historyservice.entities.HistoryEntity;
import org.historyservice.historyservice.utils.QuestionLevel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HistoryRepositoryTest {
    @Autowired
    private HistoryRepository historyRepository;

    private HistoryEntity createHistoryEntity(String question, String answer, QuestionLevel level) {
        HistoryEntity entity = new HistoryEntity();
        entity.setQuestion(question);
        entity.setAnswer(answer);
        entity.setLevel(level);
        return entity;
    }

    @Test
    public void whenAddEntityThenEntitySavesCorrectly() {
        HistoryEntity outerEntity = createHistoryEntity("question", "answer", QuestionLevel.easy);
        historyRepository.save(outerEntity);

        HistoryEntity innerEntity = historyRepository.findById(outerEntity.getId()).get();

        assertEquals(outerEntity, innerEntity);
    }

    @Test
    public void whenAddEntitiesThenWheyAllSaves() {
        List<HistoryEntity> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entities.add(createHistoryEntity("question" + i, "answer" + i, QuestionLevel.easy));
        }
        historyRepository.saveAll(entities);

        assertEquals(10, historyRepository.count());
    }

    @Test
    public void repositoryCanReturnRightIds() {
        List<HistoryEntity> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entities.add(createHistoryEntity("question" + i, "answer" + i, QuestionLevel.easy));
        }
        historyRepository.saveAll(entities);

        List<Long> expectedIds = entities.stream().map(HistoryEntity::getId).toList();

        List<Long> actualIds = historyRepository.findAllId();

        assertEquals(expectedIds, actualIds);
    }

    @Test
    public void repositoryCanReturnRightIdsByLevel() {
        List<HistoryEntity> entities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            entities.add(createHistoryEntity("question" + i, "answer" + i, QuestionLevel.medium));
        }
        historyRepository.saveAll(entities);

        List<Long> expectedIds = entities.stream().map(HistoryEntity::getId).toList();

        List<Long> actualIds = historyRepository.findIdsByLevel(QuestionLevel.medium);

        assertEquals(expectedIds, actualIds);
        assertEquals(expectedIds.size(), actualIds.size());
    }

    @Test
    public void repositoryCanReturnRightEntitiesByLevel() {
        List<HistoryEntity> easyEntities = new ArrayList<>();
        List<HistoryEntity> mediumEntities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            easyEntities.add(createHistoryEntity("question" + i, "answer" + i, QuestionLevel.easy));
        }
        for (int i = 5; i < 15; i++) {
            mediumEntities.add(createHistoryEntity("question" + i, "answer" + i, QuestionLevel.medium));
        }
        historyRepository.saveAll(easyEntities);
        historyRepository.saveAll(mediumEntities);

        List<HistoryEntity> actualEntities = historyRepository.findAllByLevel(QuestionLevel.medium);

        assertEquals(mediumEntities, actualEntities);
        assertEquals(mediumEntities.size(), actualEntities.size());
    }
}
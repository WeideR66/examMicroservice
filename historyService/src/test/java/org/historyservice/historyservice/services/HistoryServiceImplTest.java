package org.historyservice.historyservice.services;

import org.historyservice.historyservice.dto.HistoryDTO;
import org.historyservice.historyservice.entities.HistoryEntity;
import org.historyservice.historyservice.mapper.HistoryMapper;
import org.historyservice.historyservice.repositories.HistoryRepository;
import org.historyservice.historyservice.services.impl.HistoryServiceImpl;
import org.historyservice.historyservice.utils.QuestionLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka
class HistoryServiceImplTest {
    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private HistoryServiceImpl service;

    @Autowired
    private HistoryMapper historyMapper;

    private List<HistoryDTO> mathDTOs;

    @BeforeEach
    public void setUp() {
        List<HistoryEntity> mathEntities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mathEntities.add(createHistoryEntity("Question " + i, "Answer " + i, QuestionLevel.easy));
        }
        for (int i = 10; i < 20; i++) {
            mathEntities.add(createHistoryEntity("Question " + i, "Answer " + i, QuestionLevel.medium));
        }
        for (int i = 20; i < 30; i++) {
            mathEntities.add(createHistoryEntity("Question " + i, "Answer " + i, QuestionLevel.hard));
        }
        historyRepository.saveAll(mathEntities);
        mathDTOs = historyMapper.fromEntityListToDTOList(mathEntities);
    }

    @AfterEach
    public void tearDown() {
        historyRepository.deleteAll();
    }

    private HistoryEntity createHistoryEntity(String question, String answer, QuestionLevel level) {
        HistoryEntity entity = new HistoryEntity();
        entity.setQuestion(question);
        entity.setAnswer(answer);
        entity.setLevel(level);
        return entity;
    }

    @Test
    public void whenTakeRandomQuestionsThenServiceReturnQuestions() {
        List<HistoryDTO> questions = service.getRandomQuestions(10);

        assertEquals(10, questions.size());
    }

    @Test
    public void whenTakeAllQuestionsByLevelThenServiceReturnQuestions() {
        List<HistoryDTO> questions = service.getAllQuestionsByLevel(QuestionLevel.easy);
        System.out.println(questions);

        assertEquals(10, questions.size());
        assertEquals(mathDTOs.subList(0, 10), questions);
    }

    @Test
    public void whenTakeRandomQuestionsByLevelThenServiceReturnQuestions() {
        List<HistoryDTO> question = service.getRandomQuestionsByLevel(QuestionLevel.medium, 5);
        List<QuestionLevel> levels = question.stream().map(HistoryDTO::getLevel).toList();

        assertFalse(levels.contains(QuestionLevel.easy));
        assertFalse(levels.contains(QuestionLevel.hard));
        assertEquals(5, question.size());
    }

    @Test
    public void whenAddQuestionsThenServiceAddedQuestions() {
        long countBeforeAdd = historyRepository.count();

        List<HistoryDTO> questions = new ArrayList<>();
        for (int i = 123; i < 128; i++) {
            questions.add(
                    HistoryDTO.builder()
                            .question("Question " + i)
                            .answer("Answer " + i)
                            .level(QuestionLevel.medium)
                            .build()
            );
        }

        service.addNewQuestions(questions);

        Long countAfterAdd = historyRepository.count();

        assertEquals(countBeforeAdd + questions.size(), countAfterAdd);
    }
}
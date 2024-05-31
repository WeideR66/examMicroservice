package org.mathservice.mathservice.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mathservice.mathservice.dto.MathDTO;
import org.mathservice.mathservice.entities.MathEntity;
import org.mathservice.mathservice.mapper.MathMapper;
import org.mathservice.mathservice.repositories.MathRepository;
import org.mathservice.mathservice.services.impl.MathServiceImpl;
import org.mathservice.mathservice.utils.QuestionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka
class MathServiceImplTest {

    @Autowired
    private MathRepository mathRepository;

    @Autowired
    private MathServiceImpl service;

    @Autowired
    private MathMapper mathMapper;

    private List<MathDTO> mathDTOs;

    @BeforeEach
    public void setUp() {
        List<MathEntity> mathEntities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mathEntities.add(createMathEntity("Question " + i, "Answer " + i, QuestionLevel.easy));
        }
        for (int i = 10; i < 20; i++) {
            mathEntities.add(createMathEntity("Question " + i, "Answer " + i, QuestionLevel.medium));
        }
        for (int i = 20; i < 30; i++) {
            mathEntities.add(createMathEntity("Question " + i, "Answer " + i, QuestionLevel.hard));
        }
        mathRepository.saveAll(mathEntities);
        mathDTOs = mathMapper.fromEntityListToDTOList(mathEntities);
    }

    @AfterEach
    public void tearDown() {
        mathRepository.deleteAll();
    }

    private MathEntity createMathEntity(String question, String answer, QuestionLevel level) {
        MathEntity entity = new MathEntity();
        entity.setQuestion(question);
        entity.setAnswer(answer);
        entity.setLevel(level);
        return entity;
    }

    @Test
    public void whenTakeRandomQuestionsThenServiceReturnQuestions() {
        List<MathDTO> questions = service.getRandomQuestions(10);

        assertEquals(10, questions.size());
    }

    @Test
    public void whenTakeAllQuestionsByLevelThenServiceReturnQuestions() {
        List<MathDTO> questions = service.getAllQuestionsByLevel(QuestionLevel.easy);
        System.out.println(questions);

        assertEquals(10, questions.size());
        assertEquals(mathDTOs.subList(0, 10), questions);
    }

    @Test
    public void whenTakeRandomQuestionsByLevelThenServiceReturnQuestions() {
        List<MathDTO> question = service.getRandomQuestionsByLevel(QuestionLevel.medium, 5);
        List<QuestionLevel> levels = question.stream().map(MathDTO::getLevel).toList();

        assertFalse(levels.contains(QuestionLevel.easy));
        assertFalse(levels.contains(QuestionLevel.hard));
        assertEquals(5, question.size());
    }

    @Test
    public void whenAddQuestionsThenServiceAddedQuestions() {
        long countBeforeAdd = mathRepository.count();

        List<MathDTO> questions = new ArrayList<>();
        for (int i = 123; i < 128; i++) {
            questions.add(
                    MathDTO.builder()
                            .question("Question " + i)
                            .answer("Answer " + i)
                            .level(QuestionLevel.medium)
                            .build()
            );
        }

        service.addNewQuestions(questions);

        Long countAfterAdd = mathRepository.count();

        assertEquals(countBeforeAdd + questions.size(), countAfterAdd);
    }
}
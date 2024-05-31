package org.historyservice.historyservice.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.historyservice.historyservice.utils.QuestionLevel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryDTOTest {
    private HistoryDTO historyDTO;
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void globalSetUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void globalTearDown() {
        validatorFactory.close();
    }

    @Test
    public void whenBuildValidDtoThenDtoCreatesFine() {
        historyDTO = HistoryDTO.builder()
                .question("question")
                .answer("answer")
                .level(QuestionLevel.easy)
                .build();

        assertTrue(validator.validate(historyDTO).isEmpty());

        assertAll(
                () -> assertNull(historyDTO.getId()),
                () -> assertEquals("question", historyDTO.getQuestion()),
                () -> assertEquals("answer", historyDTO.getAnswer()),
                () -> assertEquals(QuestionLevel.easy, historyDTO.getLevel())
        );
    }

    @Test
    public void whenBuildDtoWithInvalidDataValidateFails() {
        historyDTO = HistoryDTO.builder()
                .question("")
                .answer(null)
                .level(QuestionLevel.easy)
                .build();

        assertFalse(validator.validate(historyDTO).isEmpty());
    }

}
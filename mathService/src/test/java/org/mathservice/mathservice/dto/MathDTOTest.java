package org.mathservice.mathservice.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mathservice.mathservice.utils.QuestionLevel;

import static org.junit.jupiter.api.Assertions.*;

class MathDTOTest {

    private MathDTO mathDTO;
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
        mathDTO = MathDTO.builder()
                .question("question")
                .answer("answer")
                .level(QuestionLevel.easy)
                .build();

        assertTrue(validator.validate(mathDTO).isEmpty());

        assertAll(
                () -> assertNull(mathDTO.getId()),
                () -> assertEquals("question", mathDTO.getQuestion()),
                () -> assertEquals("answer", mathDTO.getAnswer()),
                () -> assertEquals(QuestionLevel.easy, mathDTO.getLevel())
        );
    }

    @Test
    public void whenBuildDtoWithInvalidDataValidateFails() {
        mathDTO = MathDTO.builder()
                .question("")
                .answer(null)
                .level(QuestionLevel.easy)
                .build();

        assertFalse(validator.validate(mathDTO).isEmpty());
    }


}
package org.mathservice.mathservice.services;

import org.mathservice.mathservice.dto.MathDTO;
import org.mathservice.mathservice.utils.QuestionLevel;

import java.util.List;

public interface MathService {
    List<MathDTO> getRandomQuestions(int amount);
    List<MathDTO> getAllQuestionsByLevel(QuestionLevel level);
    void addNewQuestions(List<MathDTO> question);
    List<MathDTO> getRandomQuestionsByLevel(QuestionLevel level, int amount);
}

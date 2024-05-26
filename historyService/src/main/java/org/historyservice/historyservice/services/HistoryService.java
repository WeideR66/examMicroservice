package org.historyservice.historyservice.services;

import org.historyservice.historyservice.dto.HistoryDTO;
import org.historyservice.historyservice.utils.QuestionLevel;

import java.util.List;

public interface HistoryService {
    List<HistoryDTO> getRandomQuestions(int amount);
    List<HistoryDTO> getRandomQuestionsByLevel(QuestionLevel level, int amount);
    List<HistoryDTO> getAllQuestionsByLevel(QuestionLevel level);
    void addNewQuestions(List<HistoryDTO> questions);
}

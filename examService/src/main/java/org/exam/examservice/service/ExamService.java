package org.exam.examservice.service;

import org.exam.examservice.dto.RequestAddQuestionsDTO;
import org.exam.examservice.dto.RequestQuestionsDTO;
import org.exam.examservice.dto.ResponseQuestionsDTO;
import org.exam.examservice.responses.success.DataCreatedResponse;

import java.util.List;

public interface ExamService {
    List<ResponseQuestionsDTO> getRequestedQuestions(List<RequestQuestionsDTO> requestQuestionsDTO);
    List<String> getAvailableDisciplines();
    List<DataCreatedResponse> addNewQuestions(List<RequestAddQuestionsDTO> questions);
}

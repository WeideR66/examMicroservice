package org.exam.examservice.controllers;


import jakarta.validation.Valid;
import org.exam.examservice.dto.RequestAddQuestionsDTO;
import org.exam.examservice.dto.RequestQuestionsDTO;
import org.exam.examservice.dto.ResponseQuestionsDTO;
import org.exam.examservice.responses.success.DataCreatedResponse;
import org.exam.examservice.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExamController {

    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/disciplines")
    public ResponseEntity<List<String>> getAvailableDisciplines() {
        return ResponseEntity.ok().body(examService.getAvailableDisciplines());
    }

    @PostMapping
    public ResponseEntity<List<ResponseQuestionsDTO>> getQuestions(@RequestBody @Valid List<RequestQuestionsDTO> requestQuestionsDTOS) {
        return ResponseEntity.ok().body(examService.getRequestedQuestions(requestQuestionsDTOS));
    }

    @PostMapping("/addQuestions")
    public ResponseEntity<List<DataCreatedResponse>> addQuestions(@RequestBody @Valid List<RequestAddQuestionsDTO> requestAddQuestionsDTOS) {
        return ResponseEntity.ok().body(examService.addNewQuestions(requestAddQuestionsDTOS));
    }
}

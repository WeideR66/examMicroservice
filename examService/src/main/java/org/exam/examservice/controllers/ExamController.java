package org.exam.examservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
    @Operation(summary = "Возвращает список доступных сервисов дисциплин")
    public ResponseEntity<List<String>> getAvailableDisciplines() {
        return ResponseEntity.ok().body(examService.getAvailableDisciplines());
    }

    @PostMapping
    @Operation(
            summary = "Возвращает запрошенные вопросы",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "discipline": "math",
                                                    "level": "easy",
                                                    "amount": 10
                                                },
                                                {
                                                    "discipline": "history",
                                                    "level": "hard",
                                                    "amount": 7
                                                }
                                            ]
                                            """
                            )
                    )
            ))
    public ResponseEntity<List<ResponseQuestionsDTO>> getQuestions(@RequestBody @Valid List<RequestQuestionsDTO> requestQuestionsDTOS) {
        return ResponseEntity.ok().body(examService.getRequestedQuestions(requestQuestionsDTOS));
    }

    @PostMapping("/addQuestions")
    @Operation(
            summary = "Добавляет новые вопросы по указанным дисциплинам",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "discipline": "math",
                                                    "questions": [
                                                        {
                                                            "question": "Вопрос 1",
                                                            "answer": "Ответ 1",
                                                            "level": "easy"
                                                        },
                                                        {
                                                            "question": "Вопрос 2",
                                                            "answer": "Ответ 2",
                                                            "level": "medium"
                                                        }
                                                    ]
                                                },
                                                {
                                                    "discipline": "history",
                                                    "questions": [
                                                        {
                                                            "question": "Вопрос 3",
                                                            "answer": "Ответ 3",
                                                            "level": "hard"
                                                        }
                                                    ]
                                                }
                                            ]
                                            """
                            )
                    )
            ))
    public ResponseEntity<List<DataCreatedResponse>> addQuestions(@RequestBody @Valid List<RequestAddQuestionsDTO> requestAddQuestionsDTOS) {
        return ResponseEntity.ok().body(examService.addNewQuestions(requestAddQuestionsDTOS));
    }
}

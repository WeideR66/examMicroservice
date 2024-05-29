package org.historyservice.historyservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.historyservice.historyservice.dto.HistoryDTO;
import org.historyservice.historyservice.responses.success.DataCreatedResponse;
import org.historyservice.historyservice.services.HistoryService;
import org.historyservice.historyservice.utils.QuestionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HistoryController {

    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    @Operation(summary = "Возвращает определенное количество рандомных вопросов")
    public ResponseEntity<List<HistoryDTO>> getRandomQuestions(@RequestParam(defaultValue = "5") @Min(1) int amount) {
        return ResponseEntity.ok().body(
                historyService.getRandomQuestions(amount)
        );
    }

    @GetMapping("/{level}")
    @Operation(summary = "Вощвращает все вопросы определенной сложности")
    public ResponseEntity<List<HistoryDTO>> getAllQuestionByLevel(@PathVariable QuestionLevel level) {
        return ResponseEntity.ok().body(
                historyService.getAllQuestionsByLevel(level)
        );
    }

    @GetMapping(value = "/{level}", params = {"amount"})
    @Operation(summary = "Возвращает определенное количество вопрсоов определенной сложности")
    public ResponseEntity<List<HistoryDTO>> getRandomQuestionsByLevel(@PathVariable QuestionLevel level, @RequestParam @Min(1) int amount) {
        return ResponseEntity.ok().body(
                historyService.getRandomQuestionsByLevel(level, amount)
        );
    }

    @PostMapping
    @Operation(
            summary = "Добавляет новые вопросы",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "question": "В каком году закончилась Великая Отечественная Война?",
                                                    "answer": "1945",
                                                    "level": "easy"
                                                }
                                            ]
                                            """
                            )
                    )
            ))
    public ResponseEntity<DataCreatedResponse> addNewQuestions(@RequestBody @Valid List<HistoryDTO> questions) {
        historyService.addNewQuestions(questions);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new DataCreatedResponse(
                                HttpStatus.CREATED.value(),
                                "New questions added successfully"
                        )
                );
    }
}

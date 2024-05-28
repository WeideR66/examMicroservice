package org.mathservice.mathservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.mathservice.mathservice.dto.MathDTO;
import org.mathservice.mathservice.responses.success.DataCreatedResponse;
import org.mathservice.mathservice.services.MathService;
import org.mathservice.mathservice.utils.QuestionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class MathController {
    private final MathService mathService;

    @Autowired
    public MathController(MathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping
    public ResponseEntity<List<MathDTO>> getRandomQuestions(@RequestParam(defaultValue = "5") @Min(1) int amount) {
        return ResponseEntity.ok(mathService.getRandomQuestions(amount));
    }

    @GetMapping("/{level}")
    public ResponseEntity<List<MathDTO>> getAllQuestionsByLevel(@PathVariable QuestionLevel level) {
        return ResponseEntity.ok(mathService.getAllQuestionsByLevel(level));
    }

    @GetMapping(value = "/{level}", params = {"amount"})
    public ResponseEntity<List<MathDTO>> getRandomQuestionsByLevel(@PathVariable QuestionLevel level, @RequestParam @Min(1) int amount) {
        return ResponseEntity.ok().body(mathService.getRandomQuestionsByLevel(level, amount));
    }

    @PostMapping
    public ResponseEntity<DataCreatedResponse> addQuestions(@RequestBody @Valid List<MathDTO> questions) {
        mathService.addNewQuestions(questions);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new DataCreatedResponse(
                        HttpStatus.CREATED.value(),
                        "New questions added successfully"
                ));
    }
}

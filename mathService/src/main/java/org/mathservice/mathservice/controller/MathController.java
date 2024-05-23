package org.mathservice.mathservice.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.mathservice.mathservice.entities.MathEntity;
import org.mathservice.mathservice.services.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class MathController {
    private MathService mathService;

    @Autowired
    public MathController(MathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping("/{amount}")
    public ResponseEntity<Iterable<MathEntity>> getRandomQuestions(@PathVariable @Min(1) int amount) {
        return ResponseEntity.ok(mathService.getRandomQuestions(amount));
    }
}

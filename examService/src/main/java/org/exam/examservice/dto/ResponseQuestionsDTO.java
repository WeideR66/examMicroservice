package org.exam.examservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseQuestionsDTO {
    @Schema(name = "discipline", description = "Название дисциплины")
    private String discipline;

    @Schema(name = "questions", description = "Список вопросов")
    private Set<QuestionDTO> questions;
}

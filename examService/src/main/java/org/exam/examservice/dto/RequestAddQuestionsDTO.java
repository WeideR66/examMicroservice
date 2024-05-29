package org.exam.examservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestAddQuestionsDTO {
    @Schema(name = "discipline", description = "Название дисциплины. Обязательно")
    @NotNull(message = "Название дисциплины обязательно")
    @Size(min = 1, max = 50)
    private String discipline;

    @Schema(name = "questions", description = "Список вопросов")
    private List<QuestionDTO> questions;
}

package org.exam.examservice.dto;

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
    @NotNull(message = "Название дисциплины обязательно")
    @Size(min = 1, max = 50)
    private String discipline;
    private List<QuestionDTO> questions;
}

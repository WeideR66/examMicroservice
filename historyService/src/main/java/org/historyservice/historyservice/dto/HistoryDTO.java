package org.historyservice.historyservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.historyservice.historyservice.utils.QuestionLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryDTO {
    @Schema(name = "id", description = "ID вопроса. Необязателен.")
    private Long id;

    @Schema(name = "question", description = "Вопрос. Обязателен")
    @NotNull(message = "Необходим вопрос")
    @Size(min = 1, max = 500, message = "Вопрос может быть не дольше 500 символов")
    private String question;

    @Schema(name = "answer", description = "Ответ. Обязателен")
    @NotNull(message = "Необходим ответ")
    @Size(min = 1, max = 150, message = "Ответ может быть не больше 150 символов")
    private String answer;

    @Schema(name = "level", description = "Уровень сложности. Обязателен, доступные варианты: easy, medium, hard.")
    @NotNull(message = "Уровень может быть только: easy, medium, hard")
    private QuestionLevel level;
}

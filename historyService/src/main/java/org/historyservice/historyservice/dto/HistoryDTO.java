package org.historyservice.historyservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.historyservice.historyservice.utils.QuestionLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDTO {
    @NotNull(message = "Необходим вопрос")
    @Size(min = 1, max = 500, message = "Вопрос может быть не дольше 500 символов")
    private String question;
    @NotNull(message = "Необходим ответ")
    @Size(min = 1, max = 150, message = "Ответ может быть не больше 150 символов")
    private String answer;
    @NotNull(message = "Уровень может быть только: easy, medium, hard")
    private QuestionLevel level;
}

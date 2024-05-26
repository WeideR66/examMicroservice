package org.mathservice.mathservice.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mathservice.mathservice.utils.QuestionLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MathDTO {
    @NotNull(message = "Необходим вопрос")
    @Size(min = 1, max = 500, message = "Вопрос может быть не дольше 500 символов")
    private String question;
    @NotNull(message = "Необходим ответ")
    @Size(min = 1, max = 50, message = "Ответ может быть не больше 50 символов")
    private String answer;
    @NotNull(message = "Уровень может быть только: easy, medium, hard")
    private QuestionLevel level;
}

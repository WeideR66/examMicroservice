package org.exam.examservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.exam.examservice.utils.QuestionLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestQuestionsDTO {
    @NotNull(message = "Название дисциплины обязательно")
    @Size(min = 1, max = 50)
    private String discipline;
    private QuestionLevel level;
    private Integer amount;

    public String getUrl() {
        StringBuilder url = new StringBuilder("http://");
        url.append(discipline.toUpperCase());
        url.append("/api");
        if (level != null) {
            url.append("/");
            url.append(level);
        }
        if (amount != null) {
            url.append(String.format("?amount=%d", amount));
        }
        return url.toString();
    }
}

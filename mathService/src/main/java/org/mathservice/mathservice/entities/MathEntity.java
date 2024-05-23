package org.mathservice.mathservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mathservice.mathservice.utils.QuestionLevel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "math")
public class MathEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    @Size(max = 500)
    private String question;
    @NotNull
    @Size(max = 50)
    private String answer;
    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestionLevel level;
}

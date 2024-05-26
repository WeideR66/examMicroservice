package org.mathservice.mathservice.entities;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    private String answer;
    @Enumerated(EnumType.STRING)
    private QuestionLevel level;
}

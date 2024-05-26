package org.historyservice.historyservice.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.historyservice.historyservice.utils.QuestionLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "history")
public class HistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    private String answer;
    @Enumerated(EnumType.STRING)
    private QuestionLevel level;
}

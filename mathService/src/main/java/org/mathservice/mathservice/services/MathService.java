package org.mathservice.mathservice.services;

import org.mathservice.mathservice.entities.MathEntity;

public interface MathService {
    Iterable<MathEntity> getRandomQuestions(int amount);
}

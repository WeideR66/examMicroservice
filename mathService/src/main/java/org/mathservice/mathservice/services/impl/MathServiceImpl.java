package org.mathservice.mathservice.services.impl;

import org.mathservice.mathservice.entities.MathEntity;
import org.mathservice.mathservice.repositories.MathRepository;
import org.mathservice.mathservice.services.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class MathServiceImpl implements MathService {

    private final MathRepository mathRepository;

    @Autowired
    public MathServiceImpl(MathRepository mathRepository) {
        this.mathRepository = mathRepository;
    }

    @Override
    public Iterable<MathEntity> getRandomQuestions(int amount) {
        Random rand = new Random();

        long all_questions = mathRepository.count();
        if (all_questions <= amount) {
            return mathRepository.findAll();
        }

        Set<Long> ids = new HashSet<>();
        while (ids.size() < amount) {
            ids.add(rand.nextLong(1, all_questions));
        }

        return mathRepository.findAllById(ids);
    }
}

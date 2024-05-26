package org.mathservice.mathservice.services.impl;

import org.mathservice.mathservice.dto.MathDTO;
import org.mathservice.mathservice.mapper.MathMapper;
import org.mathservice.mathservice.repositories.MathRepository;
import org.mathservice.mathservice.services.MathService;
import org.mathservice.mathservice.utils.QuestionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MathServiceImpl implements MathService {

    private final MathRepository mathRepository;
    private final MathMapper mapper;

    @Autowired
    public MathServiceImpl(MathRepository mathRepository, MathMapper mapper) {
        this.mathRepository = mathRepository;
        this.mapper = mapper;
    }

    @Override
    public List<MathDTO> getRandomQuestions(int amount) {
        List<Long> ids = mathRepository.findAllId();

        if (ids.size() <= amount) {
            return mapper.fromEntityIterableToDTOList(mathRepository.findAll());
        }

        Collections.shuffle(ids);

        return mapper.fromEntityIterableToDTOList(
                mathRepository.findAllById(
                        ids.subList(0, amount)
                ));
    }

    @Override
    public List<MathDTO> getAllQuestionsByLevel(QuestionLevel level) {
        return mapper.fromEntityListToDTOList(mathRepository.findAllByLevel(level));
    }

    @Override
    public List<MathDTO> getRandomQuestionsByLevel(QuestionLevel level, int amount) {
        List<Long> ids = mathRepository.findIdsByLevel(level);

        if (ids.size() <= amount) {
            return mapper.fromEntityIterableToDTOList(mathRepository.findAllByLevel(level));
        }

        Collections.shuffle(ids);

        return mapper.fromEntityIterableToDTOList(
                mathRepository.findByLevelAndIdIn(
                        level, ids.subList(0, amount)
                ));
    }

    @Override
    @Transactional
    public void addNewQuestions(List<MathDTO> questions) {
        mathRepository.saveAll(mapper.fromDTOListToEntityList(questions));
    }
}

package org.mathservice.mathservice.services.impl;

import org.mathservice.mathservice.dto.LogDTO;
import org.mathservice.mathservice.dto.MathDTO;
import org.mathservice.mathservice.mapper.MathMapper;
import org.mathservice.mathservice.repositories.MathRepository;
import org.mathservice.mathservice.services.MathService;
import org.mathservice.mathservice.utils.QuestionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.logging.Level;

@Service
public class MathServiceImpl implements MathService {

    private final MathRepository mathRepository;
    private final MathMapper mapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${spring.kafka.kafka-topic.producer}")
    private String logTopic;

    @Autowired
    public MathServiceImpl(MathRepository mathRepository, MathMapper mapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.mathRepository = mathRepository;
        this.mapper = mapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<MathDTO> getRandomQuestions(int amount) {
        List<Long> ids = mathRepository.findAllId();

        if (ids.size() <= amount) {
            sendLog(
                    Level.INFO,
                    "Были выданы все вопросы по математике"
            );
            return mapper.fromEntityIterableToDTOList(mathRepository.findAll());
        }

        Collections.shuffle(ids);

        sendLog(
                Level.INFO,
                String.format("Было выдано %d рандомных вопросов по математике", amount)
        );
        return mapper.fromEntityIterableToDTOList(
                mathRepository.findAllById(
                        ids.subList(0, amount)
                ));
    }

    @Override
    public List<MathDTO> getAllQuestionsByLevel(QuestionLevel level) {
        sendLog(
                Level.INFO,
                String.format("Были выданы все вопросы по математике уровня %s", level.name())
        );
        return mapper.fromEntityListToDTOList(mathRepository.findAllByLevel(level));
    }

    @Override
    public List<MathDTO> getRandomQuestionsByLevel(QuestionLevel level, int amount) {
        List<Long> ids = mathRepository.findIdsByLevel(level);

        if (ids.size() <= amount) {
            return getAllQuestionsByLevel(level);
        }

        Collections.shuffle(ids);

        sendLog(
                Level.INFO,
                String.format("Было выдано %d рандомных вопросов по математике уровня %s", amount, level.name())
        );

        return mapper.fromEntityIterableToDTOList(
                mathRepository.findByLevelAndIdIn(
                        level, ids.subList(0, amount)
                ));
    }

    @Override
    @Transactional
    public void addNewQuestions(List<MathDTO> questions) {
        sendLog(
                Level.INFO,
                String.format("Было добавлено %d новых вопросов по математике", questions.size())
        );
        mathRepository.saveAll(mapper.fromDTOListToEntityList(questions));
    }

    private void sendLog(Level level, String message) {
        LogDTO log = LogDTO
                .builder()
                .level(level.getName())
                .message(message)
                .build();
        kafkaTemplate.send(logTopic, log);
    }
}

package org.historyservice.historyservice.services.impl;

import org.historyservice.historyservice.dto.HistoryDTO;
import org.historyservice.historyservice.dto.LogDTO;
import org.historyservice.historyservice.mapper.HistoryMapper;
import org.historyservice.historyservice.repositories.HistoryRepository;
import org.historyservice.historyservice.services.HistoryService;
import org.historyservice.historyservice.utils.QuestionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final HistoryMapper mapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${spring.kafka.kafka-topic.producer}")
    private String logTopic;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository, HistoryMapper mapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.historyRepository = historyRepository;
        this.mapper = mapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<HistoryDTO> getRandomQuestions(int amount) {
        List<Long> ids = historyRepository.findAllId();

        if(ids.size() <= amount) {
            sendLog(
                    Level.INFO,
                    "Были выданы все вопросы по истории"
            );
            return mapper.fromEntityIterableToDTOList(historyRepository.findAll());
        }

        Collections.shuffle(ids);

        sendLog(
                Level.INFO,
                String.format("Было выдано %d рандомных вопросов по истории", amount)
        );
        return mapper.fromEntityIterableToDTOList(historyRepository.findAllById(ids.subList(0, amount)));
    }

    @Override
    public List<HistoryDTO> getRandomQuestionsByLevel(QuestionLevel level, int amount) {
        List<Long> ids = historyRepository.findIdsByLevel(level);

        if(ids.size() <= amount) {

            return getAllQuestionsByLevel(level);
        }

        Collections.shuffle(ids);

        sendLog(
                Level.INFO,
                String.format("Было выдано %d вопросов по истории уровня %s", amount, level.name())
        );
        return mapper.fromEntityListToDTOList(
                historyRepository.findAllByLevelAndIdIn(level, ids.subList(0, amount))
        );
    }

    @Override
    public List<HistoryDTO> getAllQuestionsByLevel(QuestionLevel level) {
        sendLog(
                Level.INFO,
                String.format("Были выданы все вопросы по истории уровня %s", level.name())
        );
        return mapper.fromEntityListToDTOList(
                historyRepository.findAllByLevel(level)
        );
    }

    @Override
    @Transactional
    public void addNewQuestions(List<HistoryDTO> questions) {
        sendLog(
                Level.INFO,
                String.format("Было добавлено %d новых вопросов по истории", questions.size())
        );
        historyRepository.saveAll(
                mapper.fromDTOListToEntityList(questions)
        );
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

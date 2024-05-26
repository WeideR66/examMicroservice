package org.historyservice.historyservice.services.impl;

import org.historyservice.historyservice.dto.HistoryDTO;
import org.historyservice.historyservice.mapper.HistoryMapper;
import org.historyservice.historyservice.repositories.HistoryRepository;
import org.historyservice.historyservice.services.HistoryService;
import org.historyservice.historyservice.utils.QuestionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final HistoryMapper mapper;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository, HistoryMapper mapper) {
        this.historyRepository = historyRepository;
        this.mapper = mapper;
    }

    @Override
    public List<HistoryDTO> getRandomQuestions(int amount) {
        List<Long> ids = historyRepository.findAllIds();

        if(ids.size() <= amount) {
            return mapper.fromEntityIterableToDTOList(historyRepository.findAll());
        }

        Collections.shuffle(ids);

        return mapper.fromEntityIterableToDTOList(historyRepository.findAllById(ids.subList(0, amount)));
    }

    @Override
    public List<HistoryDTO> getRandomQuestionsByLevel(QuestionLevel level, int amount) {
        List<Long> ids = historyRepository.findIdsByLevel(level);

        if(ids.size() <= amount) {
            return mapper.fromEntityListToDTOList(
                    historyRepository.findAllByLevel(level)
            );
        }

        Collections.shuffle(ids);

        return mapper.fromEntityListToDTOList(
                historyRepository.findAllByLevelAndIdIn(level, ids.subList(0, amount))
        );
    }

    @Override
    public List<HistoryDTO> getAllQuestionsByLevel(QuestionLevel level) {
        return mapper.fromEntityListToDTOList(
                historyRepository.findAllByLevel(level)
        );
    }

    @Override
    @Transactional
    public void addNewQuestions(List<HistoryDTO> questions) {
        historyRepository.saveAll(
                mapper.fromDTOListToEntityList(questions)
        );
    }
}

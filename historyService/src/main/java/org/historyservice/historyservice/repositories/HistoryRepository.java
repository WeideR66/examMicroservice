package org.historyservice.historyservice.repositories;

import jakarta.validation.constraints.NotNull;
import org.historyservice.historyservice.entities.HistoryEntity;
import org.historyservice.historyservice.utils.QuestionLevel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepository extends CrudRepository<HistoryEntity, Long> {
    List<HistoryEntity> findAllByLevel(@NotNull QuestionLevel level);
    List<HistoryEntity> findAllByLevelAndIdIn(@NotNull QuestionLevel level, @NotNull List<Long> ids);

    @Query("select id from HistoryEntity")
    List<Long> findAllId();

    @Query("select id from HistoryEntity where level = :level")
    List<Long> findIdsByLevel(@Param("level") @NotNull QuestionLevel level);
}

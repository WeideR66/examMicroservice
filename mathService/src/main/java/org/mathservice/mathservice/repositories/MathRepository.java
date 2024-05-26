package org.mathservice.mathservice.repositories;

import jakarta.validation.constraints.NotNull;
import org.mathservice.mathservice.entities.MathEntity;
import org.mathservice.mathservice.utils.QuestionLevel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MathRepository extends CrudRepository<MathEntity, Long> {
    List<MathEntity> findAllByLevel(@NotNull QuestionLevel level);
    List<MathEntity> findByLevelAndIdIn(@NotNull QuestionLevel level, @NotNull List<Long> ids);

    @Query("select id from MathEntity where level = :level")
    List<Long> findIdsByLevel(@Param("level") @NotNull QuestionLevel level);

    @Query("select id from MathEntity")
    List<Long> findAllId();
}

package org.mathservice.mathservice.repositories;

import org.mathservice.mathservice.entities.MathEntity;
import org.springframework.data.repository.CrudRepository;

public interface MathRepository extends CrudRepository<MathEntity, Long> {

}

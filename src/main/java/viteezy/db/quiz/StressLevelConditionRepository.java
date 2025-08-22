package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.StressLevelCondition;

import java.util.List;
import java.util.Optional;

public interface StressLevelConditionRepository {

    Try<Optional<StressLevelCondition>> find(Long id);

    Try<List<StressLevelCondition>> findAll();

    Try<Long> save(StressLevelCondition stressLevelCondition);

}
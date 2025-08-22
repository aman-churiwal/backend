package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.PrimaryGoal;

import java.util.List;
import java.util.Optional;

public interface PrimaryGoalRepository {

    Try<Optional<PrimaryGoal>> find(Long id);

    Try<List<PrimaryGoal>> findAll();

    Try<Long> save(PrimaryGoal primaryGoal);

}
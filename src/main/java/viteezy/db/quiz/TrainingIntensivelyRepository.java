package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.TrainingIntensively;

import java.util.List;
import java.util.Optional;

public interface TrainingIntensivelyRepository {

    Try<Optional<TrainingIntensively>> find(Long id);

    Try<List<TrainingIntensively>> findAll();

    Try<Long> save(TrainingIntensively trainingIntensively);

}
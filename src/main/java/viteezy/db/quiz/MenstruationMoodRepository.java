package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.MenstruationMood;

import java.util.List;
import java.util.Optional;

public interface MenstruationMoodRepository {

    Try<Optional<MenstruationMood>> find(Long id);

    Try<List<MenstruationMood>> findAll();

    Try<Long> save(MenstruationMood menstruationMood);

}
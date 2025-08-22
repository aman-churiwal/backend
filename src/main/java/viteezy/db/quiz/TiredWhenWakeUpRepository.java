package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.TiredWhenWakeUp;

import java.util.List;
import java.util.Optional;

public interface TiredWhenWakeUpRepository {

    Try<Optional<TiredWhenWakeUp>> find(Long id);

    Try<List<TiredWhenWakeUp>> findAll();

    Try<Long> save(TiredWhenWakeUp tiredWhenWakeUp);

}
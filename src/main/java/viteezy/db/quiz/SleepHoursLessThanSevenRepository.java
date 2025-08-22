package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.SleepHoursLessThanSeven;

import java.util.List;
import java.util.Optional;

public interface SleepHoursLessThanSevenRepository {

    Try<Optional<SleepHoursLessThanSeven>> find(Long id);

    Try<List<SleepHoursLessThanSeven>> findAll();

    Try<Long> save(SleepHoursLessThanSeven sleepHoursLessThanSeven);

}
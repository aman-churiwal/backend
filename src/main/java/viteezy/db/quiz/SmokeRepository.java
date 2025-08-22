package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.Smoke;

import java.util.List;
import java.util.Optional;

public interface SmokeRepository {

    Try<Optional<Smoke>> find(Long id);

    Try<List<Smoke>> findAll();

    Try<Long> save(Smoke smoke);

}
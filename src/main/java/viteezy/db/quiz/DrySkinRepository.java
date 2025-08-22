package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DrySkin;

import java.util.List;
import java.util.Optional;

public interface DrySkinRepository {

    Try<Optional<DrySkin>> find(Long id);

    Try<List<DrySkin>> findAll();

    Try<Long> save(DrySkin drySkin);

}
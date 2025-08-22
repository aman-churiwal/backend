package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.HairType;

import java.util.List;
import java.util.Optional;

public interface HairTypeRepository {

    Try<Optional<HairType>> find(Long id);

    Try<List<HairType>> findAll();

    Try<Long> save(HairType hairType);

}
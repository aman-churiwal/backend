package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.SkinType;

import java.util.List;
import java.util.Optional;

public interface SkinTypeRepository {

    Try<Optional<SkinType>> find(Long id);

    Try<List<SkinType>> findAll();

    Try<Long> save(SkinType skinType);

}
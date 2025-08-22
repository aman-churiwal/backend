package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AllergyType;

import java.util.List;
import java.util.Optional;

public interface AllergyTypeRepository {

    Try<Optional<AllergyType>> find(Long id);

    Try<List<AllergyType>> findAll();

    Try<Long> save(AllergyType allergyType);

}
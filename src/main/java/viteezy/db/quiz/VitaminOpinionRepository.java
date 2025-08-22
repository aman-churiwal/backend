package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.VitaminOpinion;

import java.util.List;
import java.util.Optional;

public interface VitaminOpinionRepository {

    Try<Optional<VitaminOpinion>> find(Long id);

    Try<List<VitaminOpinion>> findAll();

    Try<Long> save(VitaminOpinion vitaminOpinion);

}
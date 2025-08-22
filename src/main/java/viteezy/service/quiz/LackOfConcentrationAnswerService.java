package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.LackOfConcentrationAnswer;

import java.util.Optional;
import java.util.UUID;

public interface LackOfConcentrationAnswerService {

    Either<Throwable, Optional<LackOfConcentrationAnswer>> find(Long id);

    Either<Throwable, Optional<LackOfConcentrationAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, LackOfConcentrationAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, LackOfConcentrationAnswer> update(CategorizedAnswer categorizedAnswer);

}
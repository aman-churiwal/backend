package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.HelpGoalAnswer;

import java.util.Optional;
import java.util.UUID;

public interface HelpGoalAnswerService {

    Either<Throwable, Optional<HelpGoalAnswer>> find(Long id);

    Either<Throwable, Optional<HelpGoalAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, HelpGoalAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, HelpGoalAnswer> update(CategorizedAnswer categorizedAnswer);

}
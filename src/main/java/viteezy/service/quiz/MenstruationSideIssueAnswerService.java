package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.MenstruationSideIssueAnswer;

import java.util.Optional;
import java.util.UUID;

public interface MenstruationSideIssueAnswerService {

    Either<Throwable, Optional<MenstruationSideIssueAnswer>> find(Long id);

    Either<Throwable, Optional<MenstruationSideIssueAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, MenstruationSideIssueAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, MenstruationSideIssueAnswer> update(CategorizedAnswer categorizedAnswer);

}
package viteezy.service.quiz;

import io.vavr.control.Try;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.Quiz;

import java.util.Optional;
import java.util.UUID;

public interface QuizService {

    Try<Quiz> find(@NonNull Long id);

    Try<Quiz> find(UUID externalReference);

    Try<Optional<Quiz>> findByCustomerExternalReference(UUID customerExternalReference);

    @Transactional
    Try<Quiz> save(Quiz quiz, String facebookPixel, String facebookClick, String userAgent, String userIpAddress, String referer);
}

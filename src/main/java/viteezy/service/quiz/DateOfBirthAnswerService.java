package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.DateOfBirthAnswer;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface DateOfBirthAnswerService {

    Either<Throwable, Optional<DateOfBirthAnswer>> find(Long id);

    Either<Throwable, Optional<DateOfBirthAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DateOfBirthAnswer> save(UUID quizExternalReference, LocalDate date);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DateOfBirthAnswer> update(UUID quizExternalReference, LocalDate date);

}
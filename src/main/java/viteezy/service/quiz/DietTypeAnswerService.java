package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.DietTypeAnswer;

import java.util.Optional;
import java.util.UUID;

public interface DietTypeAnswerService {

    Either<Throwable, Optional<DietTypeAnswer>> find(Long id);

    Either<Throwable, Optional<DietTypeAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DietTypeAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DietTypeAnswer> update(CategorizedAnswer categorizedAnswer);

}
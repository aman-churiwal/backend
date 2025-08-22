package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.HairTypeAnswer;

import java.util.Optional;
import java.util.UUID;

public interface HairTypeAnswerService {

    Either<Throwable, Optional<HairTypeAnswer>> find(Long id);

    Either<Throwable, Optional<HairTypeAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, HairTypeAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, HairTypeAnswer> update(CategorizedAnswer categorizedAnswer);

}
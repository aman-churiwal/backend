package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.SkinTypeAnswer;

import java.util.Optional;
import java.util.UUID;

public interface SkinTypeAnswerService {

    Either<Throwable, Optional<SkinTypeAnswer>> find(Long id);

    Either<Throwable, Optional<SkinTypeAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SkinTypeAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SkinTypeAnswer> update(CategorizedAnswer categorizedAnswer);

}
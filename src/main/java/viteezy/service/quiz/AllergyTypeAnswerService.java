package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.AllergyTypeAnswer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AllergyTypeAnswerService {

    Either<Throwable, Optional<AllergyTypeAnswer>> find(Long id);

    Either<Throwable, List<AllergyTypeAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AllergyTypeAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, Void> delete(CategorizedAnswer categorizedAnswer);

}
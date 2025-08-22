package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.DietTypeAnswer;

import java.util.Optional;
import java.util.UUID;

public interface DietTypeAnswerRepository {

    Try<Optional<DietTypeAnswer>> find(Long id);

    Try<Optional<DietTypeAnswer>> find(UUID quizExternalReference);

    Try<Long> save(DietTypeAnswer dietTypeAnswer);

    Try<Long> update(DietTypeAnswer dietTypeAnswer);
}
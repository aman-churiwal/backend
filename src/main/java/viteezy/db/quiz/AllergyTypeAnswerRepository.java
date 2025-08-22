package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.AllergyTypeAnswer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AllergyTypeAnswerRepository {

    Try<Optional<AllergyTypeAnswer>> find(Long id);

    Try<Optional<AllergyTypeAnswer>> find(UUID quizExternalReference, Long allergyId);

    Try<List<AllergyTypeAnswer>> find(UUID quizExternalReference);

    Try<Long> save(AllergyTypeAnswer allergyTypeAnswer);

    Try<Void> delete(Long id);
}
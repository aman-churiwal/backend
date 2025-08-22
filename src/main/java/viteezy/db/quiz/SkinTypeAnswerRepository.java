package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.SkinTypeAnswer;

import java.util.Optional;
import java.util.UUID;

public interface SkinTypeAnswerRepository {

    Try<Optional<SkinTypeAnswer>> find(Long id);

    Try<Optional<SkinTypeAnswer>> find(UUID quizExternalReference);

    Try<Long> save(SkinTypeAnswer skinTypeAnswer);

    Try<Long> update(SkinTypeAnswer skinTypeAnswer);
}
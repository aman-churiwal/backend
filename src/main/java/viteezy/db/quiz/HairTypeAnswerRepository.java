package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.HairTypeAnswer;

import java.util.Optional;
import java.util.UUID;

public interface HairTypeAnswerRepository {

    Try<Optional<HairTypeAnswer>> find(Long id);

    Try<Optional<HairTypeAnswer>> find(UUID quizExternalReference);

    Try<Long> save(HairTypeAnswer hairTypeAnswer);

    Try<Long> update(HairTypeAnswer hairTypeAnswer);
}
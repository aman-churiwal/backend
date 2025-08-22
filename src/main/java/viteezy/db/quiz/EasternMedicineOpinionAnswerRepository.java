package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.EasternMedicineOpinionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface EasternMedicineOpinionAnswerRepository {

    Try<Optional<EasternMedicineOpinionAnswer>> find(Long id);

    Try<Optional<EasternMedicineOpinionAnswer>> find(UUID quizExternalReference);

    Try<Long> save(EasternMedicineOpinionAnswer easternMedicineOpinionAnswer);

    Try<Long> update(EasternMedicineOpinionAnswer easternMedicineOpinionAnswer);
}
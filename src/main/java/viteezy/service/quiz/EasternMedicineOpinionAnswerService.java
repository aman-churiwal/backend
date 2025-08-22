package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.EasternMedicineOpinionAnswer;

import java.util.Optional;
import java.util.UUID;

public interface EasternMedicineOpinionAnswerService {

    Either<Throwable, Optional<EasternMedicineOpinionAnswer>> find(Long id);

    Either<Throwable, Optional<EasternMedicineOpinionAnswer>> find(UUID quizExternalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, EasternMedicineOpinionAnswer> save(CategorizedAnswer categorizedAnswer);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, EasternMedicineOpinionAnswer> update(CategorizedAnswer categorizedAnswer);

}
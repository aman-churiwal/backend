package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.EasternMedicineOpinion;

import java.util.List;
import java.util.Optional;

public interface EasternMedicineOpinionRepository {

    Try<Optional<EasternMedicineOpinion>> find(Long id);

    Try<List<EasternMedicineOpinion>> findAll();

    Try<Long> save(EasternMedicineOpinion easternMedicineOpinion);

}
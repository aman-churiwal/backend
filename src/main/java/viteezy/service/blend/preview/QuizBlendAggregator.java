package viteezy.service.blend.preview;

import io.vavr.Tuple2;
import io.vavr.control.Try;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.blend.Blend;
import viteezy.domain.quiz.Quiz;

import java.util.Optional;
import java.util.UUID;

public interface QuizBlendAggregator {
    Try<Optional<Quiz>> findByBlendExternalReference(UUID blendExternalReference);

    Try<Tuple2<Blend, Optional<Quiz>>> findByPaymentPlanExternalReference(UUID paymentPlanExternalReference);

    Try<Tuple2<Quiz, Optional<Blend>>> findByQuizExternalReference(UUID externalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Blend> saveAggregatedV2(UUID externalReference);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Blend> updateAggregatedV2(UUID blendExternalReference);
}

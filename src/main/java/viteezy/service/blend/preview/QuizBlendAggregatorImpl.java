package viteezy.service.blend.preview;

import com.google.common.base.Throwables;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.BlendRepository;
import viteezy.db.PaymentPlanRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.blend.Blend;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.blend.BlendStatus;
import viteezy.service.blend.BlendService;
import viteezy.service.blend.QuizBlendComputeService;
import viteezy.traits.EnforcePresenceTrait;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class QuizBlendAggregatorImpl implements QuizBlendAggregator, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizBlendAggregator.class);

    private final BlendRepository blendRepository;
    private final QuizRepository quizRepository;
    private final PaymentPlanRepository paymentPlanRepository;
    private final BlendService blendService;
    private final QuizBlendComputeService quizBlendComputeService;

    protected QuizBlendAggregatorImpl(BlendRepository blendRepository, QuizRepository quizRepository, PaymentPlanRepository paymentPlanRepository, BlendService blendService, QuizBlendComputeService quizBlendComputeService) {
        this.blendRepository = blendRepository;
        this.quizRepository = quizRepository;
        this.paymentPlanRepository = paymentPlanRepository;
        this.blendService = blendService;
        this.quizBlendComputeService = quizBlendComputeService;
    }

    @Override
    public Try<Optional<Quiz>> findByBlendExternalReference(UUID blendExternalReference) {
        return blendRepository.find(blendExternalReference)
                .map(this::getOptionalQuiz);
    }

    @Override
    public Try<Tuple2<Blend, Optional<Quiz>>> findByPaymentPlanExternalReference(UUID paymentPlanExternalReference) {
        return paymentPlanRepository.find(paymentPlanExternalReference)
                .flatMap(paymentPlan -> blendRepository.find(paymentPlan.blendId()))
                .map(aggregateBlendToTuple())
                .onFailure(peekException());
    }

    @Override
    public Try<Tuple2<Quiz, Optional<Blend>>> findByQuizExternalReference(UUID externalReference) {
        return quizRepository.find(externalReference)
                .flatMap(aggregateToTuple());
    }

    @Override
    public Try<Blend> saveAggregatedV2(UUID externalReference) {
        return quizRepository.find(externalReference)
                .flatMap(quiz -> blendService.create(quiz.getCustomerId(), quiz.getId(), BlendStatus.CREATED))
                .flatMap(quizBlendComputeService::compute)
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    @Override
    public Try<Blend> updateAggregatedV2(UUID blendExternalReference) {
        return blendRepository.find(blendExternalReference)
                .flatMap(quizBlendComputeService::compute)
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    private Optional<Quiz> getOptionalQuiz(Blend blend) {
        return Optional.ofNullable(blend.getQuizId())
                .flatMap(quizId -> quizRepository.find(quizId).toJavaOptional());
    }

    private Function<Blend, Tuple2<Blend, Optional<Quiz>>> aggregateBlendToTuple() {
        return blend -> new Tuple2<>(blend, getOptionalQuiz(blend));
    }

    private Function<Quiz, Try<Tuple2<Quiz, Optional<Blend>>>> aggregateToTuple() {
        return quiz -> blendService.findByQuizId(quiz.getId()).map(blendOptional -> new Tuple2<>(quiz, blendOptional));
    }

    private Consumer<Throwable> rollbackTransaction() {
        return throwable -> TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}

package viteezy.service.blend;

import com.google.common.base.Throwables;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.QuizAggregatedInformationV2;
import viteezy.domain.klaviyo.KlaviyoConstant;
import viteezy.domain.blend.BlendStatus;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.service.CustomerService;
import viteezy.service.blend.preview.QuizBlendPreviewServiceV2;
import viteezy.service.payment.PaymentPlanService;
import viteezy.service.quiz.QuizService;
import viteezy.traits.RetryTrait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class QuizBlendComputeServiceImpl implements QuizBlendComputeService, RetryTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuizBlendComputeServiceImpl.class);

    private final BlendIngredientService blendIngredientService;
    private final QuizService quizService;
    private final BlendService blendService;
    private final BlendProcessorV2 blendProcessorV2;
    private final QuizBlendPreviewServiceV2 quizBlendPreviewServiceV2;
    private final PaymentPlanService paymentPlanService;
    private final CustomerService customerService;
    private final KlaviyoService klaviyoService;

    protected QuizBlendComputeServiceImpl(
            BlendIngredientService blendIngredientService, QuizService quizService, BlendService blendService,
            BlendProcessorV2 blendProcessorV2, QuizBlendPreviewServiceV2 quizBlendPreviewServiceV2,
            PaymentPlanService paymentPlanService, CustomerService customerService, KlaviyoService klaviyoService) {
        this.blendIngredientService = blendIngredientService;
        this.quizService = quizService;
        this.blendService = blendService;
        this.blendProcessorV2 = blendProcessorV2;
        this.quizBlendPreviewServiceV2 = quizBlendPreviewServiceV2;
        this.paymentPlanService = paymentPlanService;
        this.customerService = customerService;
        this.klaviyoService = klaviyoService;
    }

    @Override
    public Try<Blend> compute(Blend blend) {
        final LocalDateTime localDateTime = LocalDateTime.now();
        return computeBlendV2(blend)
                .peek(computedBlend -> LOGGER.info("Finished computing blendId={}, blendRef={}, delta={}", computedBlend.getId(), computedBlend.getExternalReference(), Duration.between(localDateTime, LocalDateTime.now()).toMillis()))
                .onFailure(throwable -> LOGGER.error("Failure computing blendId={}, delta={}", blend.getId(), Duration.between(localDateTime, LocalDateTime.now()).toMillis(), throwable));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    private Try<Blend> computeBlendV2(Blend blend) {
        return blendIngredientService.findByBlendId(blend.getId())
                .orElse(Try.success(Collections.emptyList()))
                .flatMap(oldBlendIngredients -> cleanUpBlendIngredients(blend)
                        .flatMap(updateBlendStatus(blend, BlendStatus.PROCESSING))
                        .flatMap(computedBlend -> getQuizAggregatedInformationV2(blend).apply(computedBlend))
                        .peek(quizAggregatedInformation -> LOGGER.debug("quizAggregatedInformation={}", quizAggregatedInformation))
                        .flatMap(computeBlendIngredientsV2())
                        .flatMap(saveNewBlendIngredients(oldBlendIngredients))
                        .flatMap(updateBlendStatus(blend, BlendStatus.FINISHED))
                        .peek(this::updateBlendPaymentIfActivePaymentPlan)
                        .peek(__ -> updateKlaviyoQuizInformation(blend))
                        .onFailure(peekException())
                        .onFailure(rollbackTransaction())
                );
    }

    private void updateBlendPaymentIfActivePaymentPlan(Blend blend) {
        paymentPlanService.findActivePaymentPlanByBlendId(blend.getId())
                .peek(paymentPlan -> paymentPlanService.updateSubscription(blend, paymentPlan, paymentPlan.recurringMonths()));
    }

    private Function<Seq<BlendIngredient>, Try<Seq<BlendIngredient>>> saveNewBlendIngredients(List<BlendIngredient> oldBlendIngredients) {
        return blendIngredients -> {
            final Seq<Try<BlendIngredient>> collect = blendIngredients
                    .map(blendIngredient -> blendIngredientService.save(checkOriginalPrice(blendIngredient, oldBlendIngredients)));
            return Try.sequence(collect);
        };
    }

    private BlendIngredient checkOriginalPrice(BlendIngredient blendIngredient, List<BlendIngredient> oldBlendIngredients) {
        return oldBlendIngredients.stream()
                .filter(blendIngredient1 -> blendIngredient1.getIngredientId().equals(blendIngredient.getIngredientId()))
                .findFirst()
                .map(oldBlendIngredient -> buildWithOriginalPrice(oldBlendIngredient, blendIngredient))
                .orElse(blendIngredient);
    }

    private BlendIngredient buildWithOriginalPrice(BlendIngredient oldBlendIngredient, BlendIngredient newBlendIngredient) {
        return new BlendIngredient(null, newBlendIngredient.getIngredientId(), newBlendIngredient.getBlendId(),
                newBlendIngredient.getAmount(), newBlendIngredient.getIsUnit(), oldBlendIngredient.getPrice(),
                oldBlendIngredient.getCurrency(), newBlendIngredient.getExplanation(), newBlendIngredient.getGoal(),
                newBlendIngredient.getPriorityScore(), LocalDateTime.now(), LocalDateTime.now());
    }

    private Function<QuizAggregatedInformationV2, Try<Seq<BlendIngredient>>> computeBlendIngredientsV2() {
        return blendProcessorV2::compute;
    }

    private void updateKlaviyoQuizInformation(Blend blend) {
        customerService.find(blend.getCustomerId())
                .filter(customer -> customer.getKlaviyoProfileId() != null)
                .peek(customer -> quizService.find(blend.getQuizId())
                        .peek(quiz -> klaviyoService.upsertInitialProfile(customer, Optional.of(quiz))
                                .peek(customer1 -> klaviyoService.createEvent(customer1, KlaviyoConstant.ADDED_TO_CART, null, Optional.of(blend.getId())))));
    }

    private Function<Blend, Try<QuizAggregatedInformationV2>> getQuizAggregatedInformationV2(Blend blend) {
        return whatever -> quizBlendPreviewServiceV2.find(blend);
    }

    private <T> Function<T, Try<Blend>> updateBlendStatus(Blend blend, BlendStatus blendStatus) {
        return aVoid -> blendService.updateStatus(blend.getId(), blendStatus);
    }

    /* Assumes idempotency */
    private Try<Void> cleanUpBlendIngredients(Blend blend) {
        return blendIngredientService.deleteBulk(blend.getId());
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }

    private Consumer<Throwable> rollbackTransaction() {
        return throwable -> TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}

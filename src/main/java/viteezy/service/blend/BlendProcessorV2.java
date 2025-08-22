package viteezy.service.blend;

import io.vavr.collection.Seq;
import io.vavr.control.Try;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.QuizAggregatedInformationV2;

public interface BlendProcessorV2 {

    Try<Seq<BlendIngredient>> compute(QuizAggregatedInformationV2 quizAggregatedInformationV2);
}
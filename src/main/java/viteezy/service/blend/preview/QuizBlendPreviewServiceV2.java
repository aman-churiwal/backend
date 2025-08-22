package viteezy.service.blend.preview;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.QuizAggregatedInformationV2;

import java.util.List;
import java.util.UUID;

public interface QuizBlendPreviewServiceV2 {

    Try<QuizAggregatedInformationV2> find(Blend blend);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<List<BlendIngredient>> getBlendPreviewV2(UUID quizExternalReference);
}

package viteezy.db;

import io.vavr.control.Try;
import viteezy.domain.blend.BlendIngredientReason;
import viteezy.domain.blend.BlendIngredientReasonCode;

import java.util.List;
import java.util.Optional;

public interface BlendIngredientReasonRepository {

    Try<Optional<BlendIngredientReason>> find(Long id);

    Try<Optional<BlendIngredientReason>> find(BlendIngredientReasonCode code);

    Try<List<BlendIngredientReason>> findAll();
}
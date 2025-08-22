package viteezy.db;

import io.vavr.control.Try;
import viteezy.domain.blend.BlendIngredient;

import java.util.List;
import java.util.UUID;

public interface BlendIngredientRepository {

    Try<BlendIngredient> find(Long id);

    Try<List<BlendIngredient>> findByBlendExternalReference(UUID externalReference);

    Try<List<BlendIngredient>> findSkuByBlendExternalReference(UUID externalReference);

    Try<List<BlendIngredient>> findWithoutSkuByBlendExternalReference(UUID externalReference);

    Try<List<BlendIngredient>> findByBlendId(Long blendId);

    Try<BlendIngredient> findByBlendIdAndIngredientId(Long blendId, Long ingredientId);

    Try<Long> save(BlendIngredient blendIngredient);

    Try<Long> saveHistoryBlendIngredient(BlendIngredient blendIngredient);

    Try<Long> update(BlendIngredient blendIngredient);

    Try<Void> deleteBulk(Long blendId);

    Try<Void> delete(Long blendId, Long ingredientId);
}
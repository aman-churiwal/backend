package viteezy.service.blend;

import io.vavr.collection.Seq;
import io.vavr.control.Try;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.ingredient.Ingredient;

import java.util.List;
import java.util.UUID;

public interface BlendIngredientService {

    Try<BlendIngredient> find(Long id);

    Try<List<BlendIngredient>> findByBlendExternalReference(UUID externalReference);

    Try<List<BlendIngredient>> findByBlendId(Long blendId);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<BlendIngredient> save(Long blendId, Long ingredientId);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<BlendIngredient> save(BlendIngredient blendIngredient);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Seq<Long>> saveHistory(List<BlendIngredient> blendIngredient);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<BlendIngredient> update(BlendIngredient blendIngredient);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Void> deleteBulk(Long blendId);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Void> delete(Long blendId, Long ingredientId);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Void> deleteIfNotInProcess(Long blendId, Long ingredientId);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Seq<Ingredient>> getIngredients(Long blendId);
}

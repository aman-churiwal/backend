package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import viteezy.db.BlendIngredientRepository;
import viteezy.domain.blend.BlendIngredient;

import java.util.List;
import java.util.UUID;

public class BlendIngredientRepositoryImpl implements BlendIngredientRepository {

    private static final String SELECT_ALL = "SELECT blend_ingredients_relations.* FROM blend_ingredients_relations ";
    private static final String SELECT_ALL_BY_BLEND_EXT_REF = "" +
            "SELECT blend_ingredients_relations.* " +
            "FROM blend_ingredients_relations " +
            "JOIN blends b on blend_ingredients_relations.blend_id = b.id " +
            "JOIN ingredients i on blend_ingredients_relations.ingredient_id = i.id " +
            "WHERE b.external_reference = :externalReference ";
    private static final String INSERT_QUERY = "" +
            "INSERT INTO blend_ingredients_relations(ingredient_id, blend_id, amount, is_unit, price, currency, explanation) " +
            "VALUES(:ingredientId, :blendId, :amount, :isUnit, :price, :currency, :explanation);";
    private static final String INSERT_HISTORY_QUERY = "" +
            "INSERT INTO blend_ingredients_relations_history(ingredient_id, blend_id, amount, is_unit, price, currency, explanation) " +
            "VALUES(:ingredientId, :blendId, :amount, :isUnit, :price, :currency, :explanation);";
    private static final String UPDATE_BASE_QUERY = "" +
            "UPDATE blend_ingredients_relations " +
            "SET amount = :amount, is_unit = :isUnit, price = :price, currency = :currency, modification_timestamp = NOW() ";
    private static final String UPDATE_BY_ID = UPDATE_BASE_QUERY + "WHERE id = :id";
    private static final String DELETE_BY_BLEND_ID = "" +
            "DELETE FROM blend_ingredients_relations " +
            "WHERE blend_id = :blendId";
    private static final String DELETE_BY_BLEND_ID_AND_INGREDIENT_ID = "" +
            "DELETE FROM blend_ingredients_relations " +
            "WHERE blend_id = :blendId AND ingredient_id = :ingredientId";
    private final Jdbi jdbi;

    public BlendIngredientRepositoryImpl(Jdbi jdbi) {

        this.jdbi = jdbi;
    }

    @Override
    public Try<BlendIngredient> find(Long id) {
        final HandleCallback<BlendIngredient, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(BlendIngredient.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<BlendIngredient>> findByBlendExternalReference(UUID externalReference) {
        final HandleCallback<List<BlendIngredient>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BY_BLEND_EXT_REF)
                .bind("externalReference", externalReference)
                .mapTo(BlendIngredient.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<BlendIngredient>> findSkuByBlendExternalReference(UUID externalReference) {
        final HandleCallback<List<BlendIngredient>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BY_BLEND_EXT_REF + "AND i.sku IS NOT NULL")
                .bind("externalReference", externalReference)
                .mapTo(BlendIngredient.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<BlendIngredient>> findWithoutSkuByBlendExternalReference(UUID externalReference) {
        final HandleCallback<List<BlendIngredient>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BY_BLEND_EXT_REF + "AND i.sku IS NULL")
                .bind("externalReference", externalReference)
                .mapTo(BlendIngredient.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<BlendIngredient>> findByBlendId(Long blendId) {
        final HandleCallback<List<BlendIngredient>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE blend_id = :blendId")
                .bind("blendId", blendId)
                .mapTo(BlendIngredient.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<BlendIngredient> findByBlendIdAndIngredientId(Long blendId, Long ingredientId) {
        final HandleCallback<BlendIngredient, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE blend_id = :blendId and ingredient_id = :ingredientId")
                .bind("blendId", blendId)
                .bind("ingredientId", ingredientId)
                .mapTo(BlendIngredient.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Long> save(BlendIngredient blendIngredient) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(blendIngredient)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> saveHistoryBlendIngredient(BlendIngredient blendIngredient) {
        final HandleCallback<ResultBearing, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_HISTORY_QUERY)
                .bindBean(blendIngredient)
                .executeAndReturnGeneratedKeys();
        return Try.of(() -> jdbi.withHandle(queryCallback).mapTo(Long.class).one());
    }

    @Override
    public Try<Long> update(BlendIngredient blendIngredient) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_BY_ID)
                .bindBean(blendIngredient)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .map(affectedRows -> blendIngredient.getId());
    }

    @Override
    public Try<Void> deleteBulk(Long blendId) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(DELETE_BY_BLEND_ID)
                .bind("blendId", blendId)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(integer -> Try.success(null));
    }

    @Override
    public Try<Void> delete(Long blendId, Long ingredientId) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(DELETE_BY_BLEND_ID_AND_INGREDIENT_ID)
                .bind("blendId", blendId)
                .bind("ingredientId", ingredientId)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(integer -> Try.success(null));
    }
}

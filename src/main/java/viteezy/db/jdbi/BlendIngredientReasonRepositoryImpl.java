package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.BlendIngredientReasonRepository;
import viteezy.domain.blend.BlendIngredientReason;
import viteezy.domain.blend.BlendIngredientReasonCode;

import java.util.List;
import java.util.Optional;

public class BlendIngredientReasonRepositoryImpl implements BlendIngredientReasonRepository {

    private static final String SELECT_ALL = "SELECT blend_ingredient_reasons.* FROM blend_ingredient_reasons ";

    private final Jdbi jdbi;

    public BlendIngredientReasonRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<Optional<BlendIngredientReason>> find(Long id) {
        final HandleCallback<Optional<BlendIngredientReason>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(BlendIngredientReason.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<BlendIngredientReason>> find(BlendIngredientReasonCode code) {
        final HandleCallback<Optional<BlendIngredientReason>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :code")
                .bind("code", code)
                .mapTo(BlendIngredientReason.class).findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<BlendIngredientReason>> findAll() {
        final HandleCallback<List<BlendIngredientReason>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(BlendIngredientReason.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }
}

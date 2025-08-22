package viteezy.db.jdbi.klaviyo;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.klaviyo.KlaviyoRepository;
import viteezy.domain.klaviyo.Product;

import java.util.List;

public class KlaviyoRepositoryImpl implements KlaviyoRepository {

    private static final String SELECT_INGREDIENTS_WITH_PRICE = "SELECT ingredients.id, ingredients.name, ingredients.description, ingredients.code, ingredients.url, ingredient_prices.price " +
            "from ingredients " +
            "inner join ingredient_prices on ingredients.id = ingredient_prices.ingredient_id " +
            "where is_active " +
            "and international_system_unit != 'UNITLESS' ";

    private final Jdbi jdbi;

    public KlaviyoRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<List<Product>> findProducts() {
        final HandleCallback<List<Product>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_INGREDIENTS_WITH_PRICE)
                .mapTo(Product.class)
                .list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }


}

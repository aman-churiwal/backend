package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.BundleRepository;
import viteezy.domain.Product;
import viteezy.domain.ProductIngredient;

import java.util.List;

public class BundleRepositoryImpl implements BundleRepository {

    private static final String SELECT_ALL_PRODUCTS = "SELECT products.* FROM products ";

    private static final String SELECT_ALL_PRODUCT_INGREDIENTS = "SELECT product_ingredients.* FROM product_ingredients ";

    private final Jdbi jdbi;

    public BundleRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Product> find(String bundleCode) {
        final HandleCallback<Product, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_PRODUCTS + "WHERE code = :code")
                .bind("code", bundleCode)
                .mapTo(Product.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Product>> findAllBundles() {
        final HandleCallback<List<Product>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_PRODUCTS)
                .mapTo(Product.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<ProductIngredient>> find(Long productId) {
        final HandleCallback<List<ProductIngredient>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_PRODUCT_INGREDIENTS + "WHERE product_id = :productId")
                .bind("productId", productId)
                .mapTo(ProductIngredient.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }


}
package viteezy.db;

import io.vavr.control.Try;
import viteezy.domain.Product;
import viteezy.domain.ProductIngredient;

import java.util.List;

public interface BundleRepository {

    Try<Product> find(String bundleCode);

    Try<List<Product>> findAllBundles();

    Try<List<ProductIngredient>> find(Long productId);
}
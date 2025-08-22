package viteezy.db.klaviyo;

import io.vavr.control.Try;
import viteezy.domain.klaviyo.Product;

import java.util.List;

public interface KlaviyoRepository {

    Try<List<Product>> findProducts();
}

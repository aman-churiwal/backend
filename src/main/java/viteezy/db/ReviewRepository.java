package viteezy.db;

import io.vavr.control.Try;
import viteezy.domain.Review;

public interface ReviewRepository {

    Try<Review> summary(String source);
}

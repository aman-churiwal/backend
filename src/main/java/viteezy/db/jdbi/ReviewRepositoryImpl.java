package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.ReviewRepository;
import viteezy.domain.Review;

public class ReviewRepositoryImpl implements ReviewRepository {

    private static final String SELECT_ALL_BASE_QUERY = "SELECT * FROM reviews ";

    private final Jdbi jdbi;

    public ReviewRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<Review> summary(String source) {
        final HandleCallback<Review, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_BASE_QUERY + "WHERE source = :source")
                .bind("source", source)
                .mapTo(Review.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }
}

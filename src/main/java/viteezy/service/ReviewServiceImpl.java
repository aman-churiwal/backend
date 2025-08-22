package viteezy.service;

import com.google.common.base.Throwables;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.db.ReviewRepository;
import viteezy.domain.Review;

import java.util.function.Consumer;

public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewService.class);
    private final ReviewRepository reviewRepository;

    protected ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Try<Review> summary(String source) {
        return reviewRepository.summary(source)
                .onFailure(peekException());

    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}

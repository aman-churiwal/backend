package viteezy.db.quiz;

import io.vavr.control.Try;
import viteezy.domain.quiz.ChildrenWish;

import java.util.List;
import java.util.Optional;

public interface ChildrenWishRepository {

    Try<Optional<ChildrenWish>> find(Long id);

    Try<List<ChildrenWish>> findAll();

    Try<Long> save(ChildrenWish childrenWish);

}
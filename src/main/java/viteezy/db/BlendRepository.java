package viteezy.db;

import io.vavr.control.Try;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlendRepository {

    Try<Blend> find(UUID externalReference);

    Try<Blend> findByCustomerExternalReference(UUID customerExternalReference);

    Try<List<Blend>> findAllByCustomerExternalReference(UUID customerExternalReference);

    Try<Blend> find(Long id);

    Try<Optional<Blend>> findByQuizId(Long quizId);

    Try<Blend> save(Blend blend);

    Try<Blend> update(Blend blend);

    Try<Blend> updateStatus(Long id, BlendStatus blendStatus);
}
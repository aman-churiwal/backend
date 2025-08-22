package viteezy.service.blend;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlendService {

    Try<Blend> find(Long id);

    Try<Blend> find(UUID externalReference);

    Try<Blend> findByCustomerExternalReference(UUID customerExternalReference);

    Try<List<Blend>> findAllByCustomerExternalReference(UUID customerExternalReference);

    Try<Optional<Blend>> findByQuizId(Long quizId);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Blend> create(Long customerId, Long quizId, BlendStatus blendStatus);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Blend> update(Blend blend);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Blend> updateStatus(Long id, BlendStatus processing);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Blend> createBundle(String bundleCode);
}
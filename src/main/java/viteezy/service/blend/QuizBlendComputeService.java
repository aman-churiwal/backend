package viteezy.service.blend;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.blend.Blend;

public interface QuizBlendComputeService {

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Blend> compute(Blend blend);
}

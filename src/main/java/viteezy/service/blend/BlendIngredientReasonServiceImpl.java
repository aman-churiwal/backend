package viteezy.service.blend;

import io.vavr.control.Try;
import viteezy.db.BlendIngredientReasonRepository;
import viteezy.domain.blend.BlendIngredientReason;
import viteezy.domain.blend.BlendIngredientReasonCode;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BlendIngredientReasonServiceImpl implements BlendIngredientReasonService {

    private final BlendIngredientReasonRepository repository;

    protected BlendIngredientReasonServiceImpl(BlendIngredientReasonRepository blendIngredientReasonRepository) {
        this.repository = blendIngredientReasonRepository;
    }

    @Override
    public Try<Optional<BlendIngredientReason>> find(Long id) {
        return repository.find(id);
    }

    @Override
    public Try<Optional<BlendIngredientReason>> find(BlendIngredientReasonCode code) {
        return repository.find(code);
    }

    @Override
    public Try<List<BlendIngredientReason>> findAll() {
        return repository.findAll();
    }

    @Override
    public Try<Map<BlendIngredientReasonCode, BlendIngredientReason>> findAllAsMap() {
        return findAll()
                .map(transformListToMap());
    }

    private Function<List<BlendIngredientReason>, Map<BlendIngredientReasonCode, BlendIngredientReason>> transformListToMap() {
        return blendIngredientReasons -> blendIngredientReasons.stream()
                .collect(Collectors.toMap(BlendIngredientReason::getCode, blendIngredientReason -> blendIngredientReason));
    }
}

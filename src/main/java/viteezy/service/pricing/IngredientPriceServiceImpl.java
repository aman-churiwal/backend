package viteezy.service.pricing;

import com.google.common.base.Throwables;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.IngredientPriceRepository;
import viteezy.domain.ingredient.IngredientPrice;
import viteezy.service.IngredientService;

import java.util.List;
import java.util.function.Consumer;

public class IngredientPriceServiceImpl implements IngredientPriceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    private final IngredientPriceRepository ingredientPriceRepository;

    protected IngredientPriceServiceImpl(IngredientPriceRepository ingredientPriceRepository) {
        this.ingredientPriceRepository = ingredientPriceRepository;
    }

    @Override
    public Try<IngredientPrice> find(Long id) {
        return ingredientPriceRepository.find(id);
    }

    @Override
    public Try<IngredientPrice> findActiveByIngredientId(Long ingredientId) {
        return ingredientPriceRepository.findActiveByIngredientId(ingredientId);
    }

    @Override
    public Try<List<IngredientPrice>> findAllActive() {
        return ingredientPriceRepository.findAllActive();
    }

    @Override
    public Try<IngredientPrice> save(IngredientPrice ingredientPrice) {
        return ingredientPriceRepository
                .save(ingredientPrice)
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }

    private Consumer<Throwable> rollbackTransaction() {
        return throwable -> TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}

package viteezy.service;

import com.google.common.base.Throwables;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.db.IngredientUnitRepository;
import viteezy.domain.ingredient.*;
import java.util.List;
import java.util.function.Consumer;

public class IngredientUnitServiceImpl implements IngredientUnitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);
    private final IngredientUnitRepository ingredientUnitRepository;

    protected IngredientUnitServiceImpl(IngredientUnitRepository ingredientUnitRepository) {
        this.ingredientUnitRepository = ingredientUnitRepository;
    }

    @Override
    public Try<List<IngredientUnit>> findAllUnits() {
        return ingredientUnitRepository.findAllUnits()
                .onFailure(peekException());
    }

    @Override
    public Try<IngredientUnit> save(IngredientUnit ingredientUnit) {
        return ingredientUnitRepository
                .save(ingredientUnit)
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
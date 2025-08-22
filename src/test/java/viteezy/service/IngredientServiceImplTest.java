package viteezy.service;

import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.NoTransactionException;
import viteezy.controller.dto.IngredientGetResponse;
import viteezy.db.IngredientRepository;
import viteezy.domain.ingredient.Ingredient;
import viteezy.service.pricing.IngredientPriceService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class IngredientServiceImplTest {

    private static final Long ID = 1L;
    private static final String NAME = "NAME";
    private static final String TYPE = "TYPE";
    private static final String CODE = "CODE";
    private static final String URL = "URL";
    private static final String AMOUNT = "100g";
    private static final String PERCENTAGE = "100%";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final Boolean IS_A_FLAVOUR = false;
    private static final Boolean IS_ACTIVE = false;
    private static final String SKU = "SKU";
    private static final Integer PRIORITY = 1;
    private static final Exception EXCEPTION = new Exception("EXCEPTION");
    private static final Ingredient SAMPLE_ENTITY = new Ingredient(ID, NAME, TYPE, DESCRIPTION, CODE, URL, null, IS_A_FLAVOUR, true, PRIORITY, IS_ACTIVE, SKU, LocalDateTime.now(), LocalDateTime.now());
    private final IngredientRepository repository = mock(IngredientRepository.class);
    private final IngredientPriceService ingredientPriceService = mock(IngredientPriceService.class);
    private final IngredientUnitService ingredientUnitService = mock(IngredientUnitService.class);
    private IngredientServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new IngredientServiceImpl(repository, ingredientPriceService, ingredientUnitService);
    }


    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repository);
        reset(repository);
    }

    @Test
    void find() {
        when(repository.find(ID))
                .thenReturn(Try.success(SAMPLE_ENTITY));

        Try<Ingredient> either = service.find(ID);
        Assertions.assertTrue(either.isSuccess());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get());

        verify(repository).find(ID);
    }

    @Test
    void findException() {
        when(repository.find(ID))
                .thenReturn(Try.failure(EXCEPTION));

        Try<Ingredient> either = service.find(ID);
        Assertions.assertTrue(either.isFailure());
        Assertions.assertEquals(EXCEPTION, either.getCause());

        verify(repository).find(ID);
    }

    @Test
    void findAllOk() {
        when(repository.findAll())
                .thenReturn(Try.of(() -> Collections.singletonList(SAMPLE_ENTITY)));

        Try<List<Ingredient>> either = service.findAll();
        Assertions.assertTrue(either.isSuccess());
        Assertions.assertFalse(either.get().isEmpty());
        Assertions.assertEquals(1, either.get().size());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get().get(0));

        verify(repository).findAll();
    }


    @Test
    void findAllEmpty() {
        when(repository.findAll())
                .thenReturn(Try.of(Collections::emptyList));

        Try<List<Ingredient>> either = service.findAll();
        Assertions.assertTrue(either.isSuccess());
        Assertions.assertTrue(either.get().isEmpty());

        verify(repository).findAll();
    }

    @Test
    void findAllException() {
        when(repository.findAll())
                .thenReturn(Try.failure(EXCEPTION));

        Try<List<Ingredient>> either = service.findAll();
        Assertions.assertTrue(either.isFailure());
        Assertions.assertEquals(EXCEPTION, either.getCause());

        verify(repository).findAll();
    }

    @Test
    void findComponentsException() {
        when(repository.findAll())
                .thenReturn(Try.of(() -> Collections.singletonList(SAMPLE_ENTITY)));
        when(repository.findComponents(SAMPLE_ENTITY.getId()))
                .thenReturn(Try.failure(EXCEPTION));

        Try<List<IngredientGetResponse>> either = service.findAllWithComponents();
        Assertions.assertTrue(either.isFailure());
        Assertions.assertEquals(EXCEPTION, either.getCause());

        verify(repository).findAll();
        verify(repository).findComponents(SAMPLE_ENTITY.getId());
    }

    @Test
    void save() {
        when(repository.save(SAMPLE_ENTITY))
                .thenReturn(Try.success(SAMPLE_ENTITY));

        Try<Ingredient> either = service.save(SAMPLE_ENTITY);
        Assertions.assertTrue(either.isSuccess());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get());

        verify(repository).save(SAMPLE_ENTITY);
    }

    @Test
    void saveExceptionAtSave() {
        when(repository.save(SAMPLE_ENTITY))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertThrows(NoTransactionException.class, () -> service.save(SAMPLE_ENTITY));

        verify(repository).save(SAMPLE_ENTITY);
        verify(repository, times(0)).find(ID);
    }

    @Test
    void saveExceptionAtRetrieve() {
        when(repository.save(SAMPLE_ENTITY))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertThrows(NoTransactionException.class, () -> service.save(SAMPLE_ENTITY));

        verify(repository).save(SAMPLE_ENTITY);
    }

}
package viteezy.service.blend;

import io.vavr.control.Try;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.transaction.NoTransactionException;
import viteezy.db.BlendRepository;
import viteezy.db.BundleRepository;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendStatus;
import viteezy.service.pricing.IngredientPriceService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

class BlendServiceImplTest {

    private static final Long ID = 1L;
    private static final Long CUSTOMER_ID = 1L;
    private static final Long QUIZ_ID = 1L;
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final BlendStatus BLEND_STATUS = BlendStatus.CREATED;
    private static final UUID EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final Blend SAMPLE_ENTITY = new Blend(ID, BLEND_STATUS, EXTERNAL_REFERENCE, CUSTOMER_ID, QUIZ_ID, LocalDateTime.now(), LocalDateTime.now());

    private final IngredientPriceService ingredientPriceService = Mockito.mock(IngredientPriceService.class);
    private final BlendIngredientService blendIngredientService = Mockito.mock(BlendIngredientService.class);
    private final BlendIngredientPriceService blendIngredientPriceService = Mockito.mock(BlendIngredientPriceService.class);
    private final BlendRepository repository = Mockito.mock(BlendRepository.class);
    private final BundleRepository bundleRepository = Mockito.mock(BundleRepository.class);
    private BlendServiceImpl service;

    @BeforeEach
    void setUp() {
        Mockito.reset(repository);
        service = new BlendServiceImpl(ingredientPriceService, blendIngredientService, blendIngredientPriceService, repository, bundleRepository);
    }

    @Test
    void find() {
        Mockito.when(repository.find(ID))
                .thenReturn(Try.success(SAMPLE_ENTITY));

        Try<Blend> either = service.find(ID);
        Assertions.assertTrue(either.isSuccess());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get());

        Mockito.verify(repository).find(ID);
    }

    @Test
    void findException() {
        Mockito.when(repository.find(ID))
                .thenReturn(Try.failure(EXCEPTION));

        Try<Blend> either = service.find(ID);
        Assertions.assertTrue(either.isFailure());
        Assertions.assertEquals(EXCEPTION, either.getCause());

        Mockito.verify(repository).find(ID);
    }

    @Test
    void findExternalReference() {
        Mockito.when(repository.find(EXTERNAL_REFERENCE))
                .thenReturn(Try.success(SAMPLE_ENTITY));

        Try<Blend> either = service.find(EXTERNAL_REFERENCE);
        Assertions.assertTrue(either.isSuccess());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get());

        Mockito.verify(repository).find(EXTERNAL_REFERENCE);
    }

    @Test
    void findExternalReferenceException() {
        Mockito.when(repository.find(EXTERNAL_REFERENCE))
                .thenReturn(Try.failure(EXCEPTION));

        Try<Blend> either = service.find(EXTERNAL_REFERENCE);
        Assertions.assertTrue(either.isFailure());
        Assertions.assertEquals(EXCEPTION, either.getCause());

        Mockito.verify(repository).find(EXTERNAL_REFERENCE);
    }

    @Test
    void create() {
        Mockito.when(repository.save(any(Blend.class)))
                .thenReturn(Try.success(SAMPLE_ENTITY));

        Try<Blend> either = service.create(CUSTOMER_ID, QUIZ_ID, BlendStatus.CREATED);
        Assertions.assertTrue(either.isSuccess());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get());

        Mockito.verify(repository).save(any(Blend.class));
    }

    @Test
    void saveExceptionAtSave() {
        Mockito.when(repository.save(any(Blend.class)))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertThrows(NoTransactionException.class, () -> service.create(CUSTOMER_ID, QUIZ_ID, BlendStatus.CREATED));

        Mockito.verify(repository).save(any(Blend.class));
        Mockito.verify(repository, Mockito.times(0)).find(ID);
    }

    @Test
    void saveExceptionAtRetrieve() {
        Mockito.when(repository.save(any(Blend.class)))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertThrows(NoTransactionException.class, () -> service.create(CUSTOMER_ID, QUIZ_ID, BlendStatus.CREATED));

        Mockito.verify(repository).save(any(Blend.class));
    }
}
package viteezy.service.quiz;

import io.vavr.control.Either;
import io.vavr.control.Try;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.transaction.NoTransactionException;
import viteezy.db.quiz.AmountOfMeatConsumptionRepository;
import viteezy.domain.quiz.AmountOfMeatConsumption;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class AmountOfMeatConsumptionServiceImplTest {

    private static final Long ID = 1L;
    private static final String NAME = "NAME";
    private static final String CODE = "123";
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final AmountOfMeatConsumption SAMPLE_ENTITY = new AmountOfMeatConsumption(ID, NAME, CODE, true, LocalDateTime.now(), LocalDateTime.now());

    private final AmountOfMeatConsumptionRepository repository = Mockito.mock(AmountOfMeatConsumptionRepository.class);
    private AmountOfMeatConsumptionServiceImpl service;

    @BeforeEach
    void setUp() {
        Mockito.reset(repository);
        service = new AmountOfMeatConsumptionServiceImpl(repository);
    }

    @Test
    void find() {
        Mockito.when(repository.find(ID))
                .thenReturn(Try.of(() -> Optional.of(SAMPLE_ENTITY)));

        Either<Throwable, Optional<AmountOfMeatConsumption>> either = service.find(ID);
        Assertions.assertTrue(either.isRight());
        Assertions.assertTrue(either.get().isPresent());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get().get());

        Mockito.verify(repository).find(ID);
    }

    @Test
    void findNotFound() {
        Mockito.when(repository.find(ID))
                .thenReturn(Try.of(Optional::empty));

        Either<Throwable, Optional<AmountOfMeatConsumption>> either = service.find(ID);
        Assertions.assertTrue(either.isRight());
        Assertions.assertTrue(either.get().isEmpty());

        Mockito.verify(repository).find(ID);
    }

    @Test
    void findException() {
        Mockito.when(repository.find(ID))
                .thenReturn(Try.failure(EXCEPTION));

        Either<Throwable, Optional<AmountOfMeatConsumption>> either = service.find(ID);
        Assertions.assertTrue(either.isLeft());
        Assertions.assertEquals(EXCEPTION, either.getLeft());

        Mockito.verify(repository).find(ID);
    }

    @Test
    void findAllOk() {

        Mockito.when(repository.findAll())
                .thenReturn(Try.of(() -> Collections.singletonList(SAMPLE_ENTITY)));

        Either<Throwable, List<AmountOfMeatConsumption>> either = service.findAll();
        Assertions.assertTrue(either.isRight());
        Assertions.assertFalse(either.get().isEmpty());
        Assertions.assertEquals(1, either.get().size());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get().get(0));

        Mockito.verify(repository).findAll();
    }


    @Test
    void findAllEmpty() {
        Mockito.when(repository.findAll())
                .thenReturn(Try.of(Collections::emptyList));

        Either<Throwable, List<AmountOfMeatConsumption>> either = service.findAll();
        Assertions.assertTrue(either.isRight());
        Assertions.assertTrue(either.get().isEmpty());

        Mockito.verify(repository).findAll();
    }

    @Test
    void findAllException() {
        Mockito.when(repository.findAll())
                .thenReturn(Try.failure(EXCEPTION));

        Either<Throwable, List<AmountOfMeatConsumption>> either = service.findAll();
        Assertions.assertTrue(either.isLeft());
        Assertions.assertEquals(EXCEPTION, either.getLeft());

        Mockito.verify(repository).findAll();
    }

    @Test
    void save() {
        Mockito.when(repository.save(SAMPLE_ENTITY))
                .thenReturn(Try.of(() -> ID));
        Mockito.when(repository.find(ID))
                .thenReturn(Try.of(() -> Optional.of(SAMPLE_ENTITY)));

        Either<Throwable, AmountOfMeatConsumption> either = service.save(SAMPLE_ENTITY);
        Assertions.assertTrue(either.isRight());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get());

        Mockito.verify(repository).save(SAMPLE_ENTITY);
        Mockito.verify(repository).find(ID);
    }

    @Test
    void saveExceptionAtSave() {
        Mockito.when(repository.save(SAMPLE_ENTITY))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertThrows(NoTransactionException.class, () -> service.save(SAMPLE_ENTITY));

        Mockito.verify(repository).save(SAMPLE_ENTITY);
        Mockito.verify(repository, Mockito.times(0)).find(ID);
    }

    @Test
    void saveExceptionAtRetrieve() {
        Mockito.when(repository.save(SAMPLE_ENTITY))
                .thenReturn(Try.of(() -> ID));
        Mockito.when(repository.find(ID))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertThrows(NoTransactionException.class, () -> service.save(SAMPLE_ENTITY));

        Mockito.verify(repository).save(SAMPLE_ENTITY);
        Mockito.verify(repository).find(ID);
    }

}
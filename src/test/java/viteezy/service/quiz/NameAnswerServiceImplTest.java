package viteezy.service.quiz;

import io.vavr.control.Either;
import io.vavr.control.Try;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.transaction.NoTransactionException;
import viteezy.db.quiz.NameAnswerRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.quiz.NameAnswer;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

class NameAnswerServiceImplTest {

    private static final Long ID = 1L;
    private static final Long QUIZ_ID = 1L;
    private static final String NAME = "name";
    private static final UUID QUIZ_EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final NameAnswer SAMPLE_ENTITY = new NameAnswer(ID, QUIZ_ID, NAME, LocalDateTime.now(), LocalDateTime.now());
    private static final Quiz QUIZ = new Quiz(QUIZ_ID, QUIZ_EXTERNAL_REFERENCE, LocalDateTime.now(), LocalDateTime.now(), null, null);

    private final NameAnswerRepository repository = Mockito.mock(NameAnswerRepository.class);
    private final QuizRepository quizRepository = Mockito.mock(QuizRepository.class);
    private NameAnswerService service;

    @BeforeEach
    void setUp() {
        Mockito.reset(repository);
        service = new NameAnswerServiceImpl(repository, quizRepository);
    }

    @Test
    void find() {
        Mockito.when(repository.find(ID))
                .thenReturn(Try.of(() -> Optional.of(SAMPLE_ENTITY)));

        Either<Throwable, Optional<NameAnswer>> either = service.find(ID);
        Assertions.assertTrue(either.isRight());
        Assertions.assertTrue(either.get().isPresent());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get().get());

        Mockito.verify(repository).find(ID);
    }

    @Test
    void findNotFound() {
        Mockito.when(repository.find(ID))
                .thenReturn(Try.of(Optional::empty));

        Either<Throwable, Optional<NameAnswer>> either = service.find(ID);
        Assertions.assertTrue(either.isRight());
        Assertions.assertTrue(either.get().isEmpty());

        Mockito.verify(repository).find(ID);
    }

    @Test
    void findException() {
        Mockito.when(repository.find(ID))
                .thenReturn(Try.failure(EXCEPTION));

        Either<Throwable, Optional<NameAnswer>> either = service.find(ID);
        Assertions.assertTrue(either.isLeft());
        Assertions.assertEquals(EXCEPTION, either.getLeft());

        Mockito.verify(repository).find(ID);
    }

    @Test
    void findByQuizExternalReference() {
        Mockito.when(repository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.of(() -> Optional.of(SAMPLE_ENTITY)));

        Either<Throwable, Optional<NameAnswer>> either = service.find(QUIZ_EXTERNAL_REFERENCE);
        Assertions.assertTrue(either.isRight());
        Assertions.assertTrue(either.get().isPresent());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get().get());

        Mockito.verify(repository).find(QUIZ_EXTERNAL_REFERENCE);
    }

    @Test
    void findByQuizExternalReferenceNotFound() {
        Mockito.when(repository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.of(Optional::empty));

        Either<Throwable, Optional<NameAnswer>> either = service.find(QUIZ_EXTERNAL_REFERENCE);
        Assertions.assertTrue(either.isRight());
        Assertions.assertTrue(either.get().isEmpty());

        Mockito.verify(repository).find(QUIZ_EXTERNAL_REFERENCE);
    }

    @Test
    void findByQuizExternalReferenceException() {
        Mockito.when(repository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.failure(EXCEPTION));

        Either<Throwable, Optional<NameAnswer>> either = service.find(QUIZ_EXTERNAL_REFERENCE);
        Assertions.assertTrue(either.isLeft());
        Assertions.assertEquals(EXCEPTION, either.getLeft());

        Mockito.verify(repository).find(QUIZ_EXTERNAL_REFERENCE);
    }

    @Test
    void save() {
        Mockito.when(quizRepository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(QUIZ));
        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(Try.of(() -> ID));
        Mockito.when(repository.find(ID))
                .thenReturn(Try.of(() -> Optional.of(SAMPLE_ENTITY)));

        Either<Throwable, NameAnswer> either = service.save(QUIZ_EXTERNAL_REFERENCE, NAME);
        Assertions.assertTrue(either.isRight());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get());

        Mockito.verify(repository).save(Mockito.any());
        Mockito.verify(repository).find(ID);
        Mockito.verify(quizRepository).find(QUIZ_EXTERNAL_REFERENCE);
    }

    @Test
    void saveExceptionAtQuizService() {
        Mockito.when(quizRepository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.failure(EXCEPTION));
        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(Try.of(() -> ID));
        Mockito.when(repository.find(ID))
                .thenReturn(Try.of(() -> Optional.of(SAMPLE_ENTITY)));

        Assertions.assertThrows(NoTransactionException.class, () -> {
            service.save(QUIZ_EXTERNAL_REFERENCE, NAME);
            Mockito.verify(quizRepository).find(QUIZ_EXTERNAL_REFERENCE);
            Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
            Mockito.verify(repository, Mockito.times(0)).find(ID);
        });
    }

    @Test
    void saveExceptionAtRepoWhenSaving() {
        Mockito.when(quizRepository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(QUIZ));
        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(Try.failure(EXCEPTION));
        Mockito.when(repository.find(ID))
                .thenReturn(Try.of(() -> Optional.of(SAMPLE_ENTITY)));

        Assertions.assertThrows(NoTransactionException.class, () -> {
            service.save(QUIZ_EXTERNAL_REFERENCE,  NAME);
            Mockito.verify(quizRepository).find(QUIZ_EXTERNAL_REFERENCE);
            Mockito.verify(repository).save(Mockito.any());
            Mockito.verify(repository, Mockito.times(0)).find(ID);
        });
    }

    @Test
    void saveExceptionAtRepoWhenFetching() {
        Mockito.when(quizRepository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(QUIZ));
        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(Try.of(() -> ID));
        Mockito.when(repository.find(ID))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertThrows(NoTransactionException.class, () -> {
            service.save(QUIZ_EXTERNAL_REFERENCE, NAME);
            Mockito.verify(repository).find(QUIZ_EXTERNAL_REFERENCE);
            Mockito.verify(repository).save(Mockito.any());
            Mockito.verify(repository).find(ID);
        });
    }

    @Test
    void update() {
        Mockito.when(repository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.of(() -> Optional.of(SAMPLE_ENTITY)));
        Mockito.when(repository.update(Mockito.any()))
                .thenReturn(Try.of(() -> ID));
        Mockito.when(repository.find(ID))
                .thenReturn(Try.of(() -> Optional.of(SAMPLE_ENTITY)));

        Either<Throwable, NameAnswer> either = service.update(QUIZ_EXTERNAL_REFERENCE, NAME);
        Assertions.assertTrue(either.isRight());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get());

        Mockito.verify(repository).update(Mockito.any());
        Mockito.verify(repository).find(ID);
        Mockito.verify(repository).find(QUIZ_EXTERNAL_REFERENCE);
    }

    @Test
    void updateExceptionAtQuizService() {
        Mockito.when(repository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.of(Optional::empty));
        Mockito.when(repository.update(Mockito.any()))
                .thenReturn(Try.of(() -> ID));
        Mockito.when(repository.find(ID))
                .thenReturn(Try.of(() -> Optional.of(SAMPLE_ENTITY)));

        Assertions.assertThrows(NoTransactionException.class, () -> {
            service.update(QUIZ_EXTERNAL_REFERENCE, NAME);
            Mockito.verify(repository).find(QUIZ_EXTERNAL_REFERENCE);
            Mockito.verify(repository, Mockito.times(0)).update(Mockito.any());
            Mockito.verify(repository, Mockito.times(0)).find(ID);
        });
    }

    @Test
    void updateExceptionAtRepoWhenSaving() {
        Mockito.when(repository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.of(() -> Optional.of(SAMPLE_ENTITY)));
        Mockito.when(repository.update(Mockito.any()))
                .thenReturn(Try.failure(EXCEPTION));
        Mockito.when(repository.find(ID))
                .thenReturn(Try.of(() -> Optional.of(SAMPLE_ENTITY)));

        Assertions.assertThrows(NoTransactionException.class, () -> {
            service.update(QUIZ_EXTERNAL_REFERENCE, NAME);
            Mockito.verify(repository).find(QUIZ_EXTERNAL_REFERENCE);
            Mockito.verify(repository).update(Mockito.any());
            Mockito.verify(repository, Mockito.times(0)).find(ID);
        });
    }

    @Test
    void updateExceptionAtRepoWhenFetching() {
        Mockito.when(repository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.of(() -> Optional.of(SAMPLE_ENTITY)));
        Mockito.when(repository.update(Mockito.any()))
                .thenReturn(Try.of(() -> ID));
        Mockito.when(repository.find(ID))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertThrows(NoTransactionException.class, () -> {
            service.update(QUIZ_EXTERNAL_REFERENCE, NAME);
            Mockito.verify(repository).find(QUIZ_EXTERNAL_REFERENCE);
            Mockito.verify(repository).update(Mockito.any());
            Mockito.verify(repository).find(ID);
        });
    }
}
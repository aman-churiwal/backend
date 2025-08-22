package viteezy.service.quiz;

import io.vavr.control.Either;
import io.vavr.control.Try;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.transaction.NoTransactionException;
import viteezy.db.quiz.QuizRepository;
import viteezy.db.quiz.UsageGoalAnswerRepository;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.quiz.UsageGoalAnswer;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class UsageGoalAnswerServiceImplTest {

    private static final Long ID = 1L;
    private static final Long QUIZ_ID = 1L;
    private static final Long ALLERGY_ID = 1L;
    private static final UUID QUIZ_EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final UsageGoalAnswer SAMPLE_ENTITY = new UsageGoalAnswer(ID, QUIZ_ID, ALLERGY_ID, LocalDateTime.now(), LocalDateTime.now());
    private static final CategorizedAnswer CATEGORIZED_ANSWER = new CategorizedAnswer(QUIZ_EXTERNAL_REFERENCE, ALLERGY_ID);
    private static final Quiz QUIZ = new Quiz(QUIZ_ID, QUIZ_EXTERNAL_REFERENCE, LocalDateTime.now(), LocalDateTime.now(), null, null);

    private final UsageGoalAnswerRepository repository = Mockito.mock(UsageGoalAnswerRepository.class);
    private final QuizRepository quizRepository = Mockito.mock(QuizRepository.class);
    private UsageGoalAnswerService service;

    @BeforeEach
    void setUp() {
        Mockito.reset(repository);
        service = new UsageGoalAnswerServiceImpl(repository, quizRepository);
    }

    @Test
    void find() {
        Mockito.when(repository.find(ID))
                .thenReturn(Try.of(() -> Optional.of(SAMPLE_ENTITY)));

        Either<Throwable, Optional<UsageGoalAnswer>> either = service.find(ID);
        Assertions.assertTrue(either.isRight());
        Assertions.assertTrue(either.get().isPresent());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get().get());

        Mockito.verify(repository).find(ID);
    }

    @Test
    void findNotFound() {
        Mockito.when(repository.find(ID))
                .thenReturn(Try.of(Optional::empty));

        Either<Throwable, Optional<UsageGoalAnswer>> either = service.find(ID);
        Assertions.assertTrue(either.isRight());
        Assertions.assertTrue(either.get().isEmpty());

        Mockito.verify(repository).find(ID);
    }

    @Test
    void findException() {
        Mockito.when(repository.find(ID))
                .thenReturn(Try.failure(EXCEPTION));

        Either<Throwable, Optional<UsageGoalAnswer>> either = service.find(ID);
        Assertions.assertTrue(either.isLeft());
        Assertions.assertEquals(EXCEPTION, either.getLeft());

        Mockito.verify(repository).find(ID);
    }

    @Test
    void findByQuizExternalReference() {
        Mockito.when(repository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.of(() -> Collections.singletonList(SAMPLE_ENTITY)));

        Either<Throwable, List<UsageGoalAnswer>> either = service.find(QUIZ_EXTERNAL_REFERENCE);
        Assertions.assertTrue(either.isRight());
        Assertions.assertFalse(either.get().isEmpty());
        Assertions.assertEquals(SAMPLE_ENTITY, either.get().get(0));

        Mockito.verify(repository).find(QUIZ_EXTERNAL_REFERENCE);
    }

    @Test
    void findByQuizExternalReferenceNotFound() {
        Mockito.when(repository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.of(Collections::emptyList));

        Either<Throwable, List<UsageGoalAnswer>> either = service.find(QUIZ_EXTERNAL_REFERENCE);
        Assertions.assertTrue(either.isRight());
        Assertions.assertTrue(either.get().isEmpty());

        Mockito.verify(repository).find(QUIZ_EXTERNAL_REFERENCE);
    }

    @Test
    void findByQuizExternalReferenceException() {
        Mockito.when(repository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.failure(EXCEPTION));

        Either<Throwable, List<UsageGoalAnswer>> either = service.find(QUIZ_EXTERNAL_REFERENCE);
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

        Either<Throwable, UsageGoalAnswer> either = service.save(CATEGORIZED_ANSWER);
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
            service.save(CATEGORIZED_ANSWER);
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
            service.save(CATEGORIZED_ANSWER);
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
            service.save(CATEGORIZED_ANSWER);
            Mockito.verify(repository).find(QUIZ_EXTERNAL_REFERENCE);
            Mockito.verify(repository).save(Mockito.any());
            Mockito.verify(repository).find(ID);
        });
    }
}
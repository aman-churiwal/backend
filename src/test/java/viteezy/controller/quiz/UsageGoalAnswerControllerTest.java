package viteezy.controller.quiz;

import io.vavr.control.Either;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import viteezy.controller.dto.quiz.UsageGoalAnswerGetResponse;
import viteezy.domain.dto.CategorizedAnswer;
import viteezy.domain.quiz.UsageGoalAnswer;
import viteezy.service.quiz.UsageGoalAnswerService;

import java.time.LocalDateTime;
import java.util.*;

class UsageGoalAnswerControllerTest {

    private static final Long ID = 1L;
    private static final Long QUIZ_ID = 1L;
    private static final Long ALLERGY_ID = 1L;
    private static final UUID QUIZ_EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final UsageGoalAnswer SAMPLE_ENTITY = new UsageGoalAnswer(ID, QUIZ_ID, ALLERGY_ID, LocalDateTime.now(), LocalDateTime.now());
    private static final CategorizedAnswer CATEGORIZED_ANSWER = new CategorizedAnswer(QUIZ_EXTERNAL_REFERENCE, ALLERGY_ID);

    private final UsageGoalAnswerService service = Mockito.mock(UsageGoalAnswerService.class);
    private UsageGoalAnswerController controller;

    @BeforeEach
    void setUp() {
        Mockito.reset(service);
        controller = new UsageGoalAnswerController(service);
    }

    @Test
    void find() {
        Mockito.when(service.find(ID))
                .thenReturn(Either.right(Optional.of(SAMPLE_ENTITY)));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getById(ID);
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getId(), ((UsageGoalAnswerGetResponse)response.getEntity()).getId());

            Mockito.verify(service).find(ID);
        });
    }

    @Test
    void findNotFound() {
        Mockito.when(service.find(ID))
                .thenReturn(Either.right(Optional.empty()));

        Assertions.assertThrows(NoSuchElementException.class, () -> controller.getById(ID));

        Mockito.verify(service).find(ID);
    }

    @Test
    void findException() {
        Mockito.when(service.find(ID))
                .thenReturn(Either.left(EXCEPTION));

        final Response response = controller.getById(ID);
        Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());

        Mockito.verify(service).find(ID);
    }

    @Test
    void findByQuizExternalReference() {
        Mockito.when(service.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Either.right(List.of(SAMPLE_ENTITY)));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getByQuizExternalReference(QUIZ_EXTERNAL_REFERENCE);
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getId(), ((List<UsageGoalAnswerGetResponse>)response.getEntity()).get(0).getId());

            Mockito.verify(service).find(QUIZ_EXTERNAL_REFERENCE);
        });
    }

    @Test
    void findByQuizExternalReferenceEmptyList() {
        Mockito.when(service.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Either.right(Collections.emptyList()));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getByQuizExternalReference(QUIZ_EXTERNAL_REFERENCE);
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertTrue(((List<UsageGoalAnswerGetResponse>) response.getEntity()).isEmpty());

            Mockito.verify(service).find(QUIZ_EXTERNAL_REFERENCE);
        });

        Mockito.verify(service).find(QUIZ_EXTERNAL_REFERENCE);
    }

    @Test
    void findByQuizExternalReferenceException() {
        Mockito.when(service.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Either.left(EXCEPTION));

        final Response response = controller.getByQuizExternalReference(QUIZ_EXTERNAL_REFERENCE);
        Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());

        Mockito.verify(service).find(QUIZ_EXTERNAL_REFERENCE);
    }

    @Test
    void save() {
        Mockito.when(service.save(Mockito.any(CATEGORIZED_ANSWER.getClass())))
                .thenReturn(Either.right(SAMPLE_ENTITY));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.save(QUIZ_EXTERNAL_REFERENCE, ALLERGY_ID);
            Assertions.assertEquals(201, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getId(), ((UsageGoalAnswerGetResponse) response.getEntity()).getId());

            Mockito.verify(service).save(Mockito.any(CATEGORIZED_ANSWER.getClass()));
        });
    }

    @Test
    void saveException() {
        Mockito.when(service.save(Mockito.any(CATEGORIZED_ANSWER.getClass())))
                .thenReturn(Either.left(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.save(QUIZ_EXTERNAL_REFERENCE, ALLERGY_ID);
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(service).save(Mockito.any(CATEGORIZED_ANSWER.getClass()));
    }
}
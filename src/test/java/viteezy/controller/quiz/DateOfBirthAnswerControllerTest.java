package viteezy.controller.quiz;

import io.vavr.control.Either;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import viteezy.controller.dto.quiz.DateOfBirthAnswerGetResponse;
import viteezy.controller.dto.quiz.DateOfBirthPostRequest;
import viteezy.domain.quiz.DateOfBirthAnswer;
import viteezy.service.quiz.DateOfBirthAnswerService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

class DateOfBirthAnswerControllerTest {

    private static final Long ID = 1L;
    private static final Long QUIZ_ID = 1L;
    private static final LocalDate DATE = LocalDate.now();
    private static final UUID QUIZ_EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final DateOfBirthAnswer SAMPLE_ENTITY = new DateOfBirthAnswer(ID, QUIZ_ID, DATE, LocalDateTime.now(), LocalDateTime.now());


    private final DateOfBirthAnswerService service = Mockito.mock(DateOfBirthAnswerService.class);
    private DateOfBirthAnswerController controller;

    @BeforeEach
    void setUp() {
        Mockito.reset(service);
        controller = new DateOfBirthAnswerController(service);
    }

    @Test
    void find() {
        Mockito.when(service.find(ID))
                .thenReturn(Either.right(Optional.of(SAMPLE_ENTITY)));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getById(ID);
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getId(), ((DateOfBirthAnswerGetResponse)response.getEntity()).getId());

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
                .thenReturn(Either.right(Optional.of(SAMPLE_ENTITY)));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getByQuizExternalReference(QUIZ_EXTERNAL_REFERENCE);
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getId(), ((DateOfBirthAnswerGetResponse)response.getEntity()).getId());

            Mockito.verify(service).find(QUIZ_EXTERNAL_REFERENCE);
        });
    }

    @Test
    void findByQuizExternalReferenceNotFound() {
        Mockito.when(service.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Either.right(Optional.empty()));

        Assertions.assertThrows(NoSuchElementException.class, () -> controller.getByQuizExternalReference(QUIZ_EXTERNAL_REFERENCE));

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
        Mockito.when(service.save(QUIZ_EXTERNAL_REFERENCE, DATE))
                .thenReturn(Either.right(SAMPLE_ENTITY));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.save(QUIZ_EXTERNAL_REFERENCE, new DateOfBirthPostRequest(DATE));
            Assertions.assertEquals(201, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getId(), ((DateOfBirthAnswerGetResponse) response.getEntity()).getId());

            Mockito.verify(service).save(QUIZ_EXTERNAL_REFERENCE, DATE);
        });
    }

    @Test
    void saveException() {
        Mockito.when(service.save(QUIZ_EXTERNAL_REFERENCE, DATE))
                .thenReturn(Either.left(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.save(QUIZ_EXTERNAL_REFERENCE, new DateOfBirthPostRequest(DATE));
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(service).save(QUIZ_EXTERNAL_REFERENCE, DATE);
    }

    @Test
    void update() {
        Mockito.when(service.update(QUIZ_EXTERNAL_REFERENCE, DATE))
                .thenReturn(Either.right(SAMPLE_ENTITY));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.update(QUIZ_EXTERNAL_REFERENCE, new DateOfBirthPostRequest(DATE));
            Assertions.assertEquals(202, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getId(), ((DateOfBirthAnswerGetResponse) response.getEntity()).getId());

            Mockito.verify(service).update(QUIZ_EXTERNAL_REFERENCE, DATE);
        });
    }

    @Test
    void updateException() {
        Mockito.when(service.update(QUIZ_EXTERNAL_REFERENCE, DATE))
                .thenReturn(Either.left(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.update(QUIZ_EXTERNAL_REFERENCE, new DateOfBirthPostRequest(DATE));
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(service).update(QUIZ_EXTERNAL_REFERENCE, DATE);
    }
}
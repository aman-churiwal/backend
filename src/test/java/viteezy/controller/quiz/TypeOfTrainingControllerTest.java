package viteezy.controller.quiz;

import io.vavr.control.Either;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import viteezy.controller.dto.quiz.TypeOfTrainingGetResponse;
import viteezy.controller.dto.quiz.TypeOfTrainingPostRequest;
import viteezy.domain.quiz.TypeOfTraining;
import viteezy.service.quiz.TypeOfTrainingService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

class TypeOfTrainingControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "NAME";
    private static final String CODE = "123";
    private static final String SUBTITLE = "TEST";
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final TypeOfTraining SAMPLE_ENTITY = new TypeOfTraining(ID, NAME, CODE, true, SUBTITLE, LocalDateTime.now(), LocalDateTime.now());
    private static final TypeOfTrainingPostRequest SAMPLE_ENTITY_DTO = new TypeOfTrainingPostRequest(NAME, CODE, true, SUBTITLE);

    private final TypeOfTrainingService service = Mockito.mock(TypeOfTrainingService.class);
    private TypeOfTrainingController controller;

    @BeforeEach
    void setUp() {
        Mockito.reset(service);
        controller = new TypeOfTrainingController(service);
    }

    @Test
    void find() {
        Mockito.when(service.find(ID))
                .thenReturn(Either.right(Optional.of(SAMPLE_ENTITY)));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getById(ID);
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((TypeOfTrainingGetResponse) response.getEntity()).getName());

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
    void findAll() {
        Mockito.when(service.findAll())
                .thenReturn(Either.right(Collections.singletonList(SAMPLE_ENTITY)));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll();
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(1, ((List<TypeOfTrainingGetResponse>) response.getEntity()).size());
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((List<TypeOfTrainingGetResponse>) response.getEntity()).get(0).getName());

            Mockito.verify(service).findAll();
        });
    }

    @Test
    void findAllEmpty() {
        Mockito.when(service.findAll())
                .thenReturn(Either.right(Collections.emptyList()));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll();
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(0, ((List<TypeOfTraining>) response.getEntity()).size());

            Mockito.verify(service).findAll();
        });

    }

    @Test
    void findAllException() {
        Mockito.when(service.findAll())
                .thenReturn(Either.left(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll();
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(service).findAll();
    }


    @Test
    void save() {
        Mockito.when(service.save(Mockito.any(SAMPLE_ENTITY.getClass())))
                .thenReturn(Either.right(SAMPLE_ENTITY));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.save(SAMPLE_ENTITY_DTO);
            Assertions.assertEquals(201, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((TypeOfTraining) response.getEntity()).getName());

            Mockito.verify(service).save(Mockito.any(SAMPLE_ENTITY.getClass()));
        });
    }

    @Test
    void saveException() {
        Mockito.when(service.save(Mockito.any(SAMPLE_ENTITY.getClass())))
                .thenReturn(Either.left(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.save(SAMPLE_ENTITY_DTO);
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(service).save(Mockito.any(SAMPLE_ENTITY.getClass()));
    }
}
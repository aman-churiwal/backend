package viteezy.controller.quiz;

import io.vavr.control.Either;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import viteezy.controller.dto.quiz.AmountOfFruitConsumptionGetResponse;
import viteezy.controller.dto.quiz.AmountOfFruitConsumptionPostRequest;
import viteezy.domain.quiz.AmountOfFruitConsumption;
import viteezy.service.quiz.AmountOfFruitConsumptionService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

class AmountOfFruitConsumptionControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "NAME";
    private static final String CODE = "123";
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final AmountOfFruitConsumption SAMPLE_ENTITY = new AmountOfFruitConsumption(ID, NAME, CODE, true, LocalDateTime.now(), LocalDateTime.now());
    private static final AmountOfFruitConsumptionPostRequest SAMPLE_ENTITY_DTO = new AmountOfFruitConsumptionPostRequest(NAME, CODE, true);

    private final AmountOfFruitConsumptionService service = Mockito.mock(AmountOfFruitConsumptionService.class);
    private AmountOfFruitConsumptionController controller;

    @BeforeEach
    void setUp() {
        Mockito.reset(service);
        controller = new AmountOfFruitConsumptionController(service);
    }

    @Test
    void find() {
        Mockito.when(service.find(ID))
                .thenReturn(Either.right(Optional.of(SAMPLE_ENTITY)));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getById(ID);
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((AmountOfFruitConsumptionGetResponse) response.getEntity()).getName());

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
            Assertions.assertEquals(1, ((List<AmountOfFruitConsumptionGetResponse>) response.getEntity()).size());
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((List<AmountOfFruitConsumptionGetResponse>) response.getEntity()).get(0).getName());

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
            Assertions.assertEquals(0, ((List<AmountOfFruitConsumption>) response.getEntity()).size());

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
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((AmountOfFruitConsumption) response.getEntity()).getName());

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
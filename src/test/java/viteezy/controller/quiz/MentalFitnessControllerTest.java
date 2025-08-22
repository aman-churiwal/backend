package viteezy.controller.quiz;

import io.vavr.control.Either;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import viteezy.controller.dto.quiz.MentalFitnessGetResponse;
import viteezy.controller.dto.quiz.MentalFitnessPostRequest;
import viteezy.domain.quiz.MentalFitness;
import viteezy.service.quiz.MentalFitnessService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

class MentalFitnessControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "NAME";
    private static final String CODE = "123";
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final MentalFitness SAMPLE_ENTITY = new MentalFitness(ID, NAME, CODE, LocalDateTime.now(), LocalDateTime.now());
    private static final MentalFitnessPostRequest SAMPLE_ENTITY_DTO = new MentalFitnessPostRequest(NAME, CODE);

    private final MentalFitnessService service = Mockito.mock(MentalFitnessService.class);
    private MentalFitnessController controller;

    @BeforeEach
    void setUp() {
        Mockito.reset(service);
        controller = new MentalFitnessController(service);
    }

    @Test
    void find() {
        Mockito.when(service.find(ID))
                .thenReturn(Either.right(Optional.of(SAMPLE_ENTITY)));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getById(ID);
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((MentalFitnessGetResponse) response.getEntity()).getName());

            Mockito.verify(service).find(ID);
        });
    }

    @Test
    void findNotFound() {
        Mockito.when(service.find(ID))
                .thenReturn(Either.right(Optional.empty()));

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            controller.getById(ID);
        });

        Mockito.verify(service).find(ID);
    }

    @Test
    void findException() {
        Mockito.when(service.find(ID))
                .thenReturn(Either.left(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getById(ID);
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(service).find(ID);
    }

    @Test
    void findAll() {
        Mockito.when(service.findAll())
                .thenReturn(Either.right(Collections.singletonList(SAMPLE_ENTITY)));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll();
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(1, ((List<MentalFitnessGetResponse>) response.getEntity()).size());
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((List<MentalFitnessGetResponse>) response.getEntity()).get(0).getName());

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
            Assertions.assertEquals(0, ((List<MentalFitness>) response.getEntity()).size());

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
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((MentalFitness) response.getEntity()).getName());

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
package viteezy.controller;

import io.vavr.control.Try;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import viteezy.controller.dto.IngredientGetResponse;
import viteezy.controller.dto.IngredientPostRequest;
import viteezy.domain.ingredient.Ingredient;
import viteezy.service.IngredientService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

class IngredientControllerTest {

    private static final Long ID = 1L;
    private static final String NAME = "NAME";
    private static final String CLAIM = "CLAIM";
    private static final String CODE = "CODE";
    private static final String URL = "URL";
    private static final String TYPE = "TYPE";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String EXCIPIENTS = "EXCIPIENTS";
    private static final Boolean IS_A_FLAVOUR = false;
    private static final Boolean IS_ACTIVE = false;
    private static final String SKU = "SKU";
    private static final Integer PRIORITY = 1;
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final Ingredient SAMPLE_ENTITY = new Ingredient(ID, NAME, TYPE, DESCRIPTION, CODE, URL, null, IS_A_FLAVOUR, true, PRIORITY, IS_ACTIVE, SKU, LocalDateTime.now(), LocalDateTime.now());
    private static final IngredientGetResponse SAMPLE_GET_ENTITY = new IngredientGetResponse(ID, NAME, TYPE, DESCRIPTION, null, null, CODE, URL, null, IS_A_FLAVOUR, true, PRIORITY, IS_ACTIVE, SKU, LocalDateTime.now(), LocalDateTime.now());
    private static final IngredientPostRequest SAMPLE_ENTITY_DTO = new IngredientPostRequest(NAME, TYPE, DESCRIPTION, EXCIPIENTS, CLAIM, CODE, URL, IS_A_FLAVOUR, true, PRIORITY, IS_ACTIVE, SKU);

    private final IngredientService service = Mockito.mock(IngredientService.class);
    private IngredientController controller;

    @BeforeEach
    void setUp() {
        Mockito.reset(service);
        controller = new IngredientController(service);
    }

    @Test
    void find() {
        Mockito.when(service.findWithComponentsAndContent(ID))
                .thenReturn(Try.success(SAMPLE_GET_ENTITY));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getById(ID);
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getId(), ((IngredientGetResponse) response.getEntity()).getId());
            Assertions.assertEquals(SAMPLE_ENTITY.getIsAFlavour(), ((IngredientGetResponse) response.getEntity()).isAFlavour());
            Assertions.assertEquals(SAMPLE_ENTITY.getDescription(), ((IngredientGetResponse) response.getEntity()).getDescription());
            Assertions.assertEquals(SAMPLE_ENTITY.getCode(), ((IngredientGetResponse) response.getEntity()).getCode());
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((IngredientGetResponse) response.getEntity()).getName());

            Mockito.verify(service).findWithComponentsAndContent(ID);
        });
    }

    @Test
    void findException() {
        Mockito.when(service.findWithComponentsAndContent(ID))
                .thenReturn(Try.failure(EXCEPTION));

        final Response response = controller.getById(ID);
        Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());

        Mockito.verify(service).findWithComponentsAndContent(ID);
    }

    @Test
    void findAll() {
        Mockito.when(service.findAllWithComponents())
                .thenReturn(Try.success(Collections.singletonList(SAMPLE_GET_ENTITY)));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll();
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(1, ((List<IngredientGetResponse>) response.getEntity()).size());
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((List<IngredientGetResponse>) response.getEntity()).get(0).getName());

            Mockito.verify(service).findAllWithComponents();
        });
    }

    @Test
    void findAllEmpty() {
        Mockito.when(service.findAllWithComponents())
                .thenReturn(Try.success(Collections.emptyList()));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll();
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(0, ((List<IngredientGetResponse>) response.getEntity()).size());

            Mockito.verify(service).findAllWithComponents();
        });

    }

    @Test
    void findAllException() {
        Mockito.when(service.findAllWithComponents())
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll();
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(service).findAllWithComponents();
    }


    @Test
    void save() {
        Mockito.when(service.save(Mockito.any(SAMPLE_ENTITY.getClass())))
                .thenReturn(Try.success(SAMPLE_ENTITY));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.save(SAMPLE_ENTITY_DTO);
            Assertions.assertEquals(201, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getName(), ((Ingredient) response.getEntity()).getName());

            Mockito.verify(service).save(Mockito.any(SAMPLE_ENTITY.getClass()));
        });
    }

    @Test
    void saveException() {
        Mockito.when(service.save(Mockito.any(SAMPLE_ENTITY.getClass())))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.save(SAMPLE_ENTITY_DTO);
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(service).save(Mockito.any(SAMPLE_ENTITY.getClass()));
    }
}
package viteezy.controller;

import io.vavr.control.Try;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import viteezy.controller.dto.BlendIngredientGetResponse;
import viteezy.controller.dto.BlendIngredientPostRequest;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.blend.BlendStatus;
import viteezy.service.blend.BlendIngredientService;
import viteezy.service.blend.BlendService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class BlendIngredientControllerTest {

    private static final Long ID = 1L;
    private static final Long BLEND_ID = 1L;
    private static final Long INGREDIENT_ID = 1L;
    private static final BigDecimal AMOUNT = new BigDecimal("10");
    private static final BigDecimal PRICE = new BigDecimal("10");
    private static final Long CUSTOMER_ID = 1L;
    private static final Long QUIZ_ID = 1L;
    private static final String IS_UNIT = "t";
    private static final String CURRENCY = "CURRENCY";
    private static final UUID EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final String EXPLANATION = "EXPLANATION";
    private static final BlendIngredient SAMPLE_ENTITY = new BlendIngredient(ID, INGREDIENT_ID, BLEND_ID, AMOUNT, IS_UNIT, PRICE, CURRENCY, EXPLANATION, LocalDateTime.now(), LocalDateTime.now());
    private static final BlendIngredientPostRequest SAMPLE_ENTITY_DTO = new BlendIngredientPostRequest(AMOUNT, IS_UNIT);
    private static final Blend BLEND_SAMPLE_ENTITY = new Blend(BLEND_ID, BlendStatus.CREATED, EXTERNAL_REFERENCE, CUSTOMER_ID, QUIZ_ID, LocalDateTime.now(), LocalDateTime.now());

    private final BlendIngredientService blendIngredientService = Mockito.mock(BlendIngredientService.class);
    private final BlendService blendService = Mockito.mock(BlendService.class);
    private BlendIngredientController controller;


    @BeforeEach
    void setUp() {
        Mockito.reset(blendIngredientService, blendService);
        this.controller = new BlendIngredientController(blendIngredientService, blendService);
    }

    @Test
    void getById() {
        Mockito.when(blendIngredientService.find(ID))
                .thenReturn(Try.success(SAMPLE_ENTITY));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getById(ID);
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getId(), ((BlendIngredientGetResponse) response.getEntity()).getId());
            Assertions.assertEquals(SAMPLE_ENTITY.getExplanation(), ((BlendIngredientGetResponse) response.getEntity()).getExplanation());

            Mockito.verify(blendIngredientService).find(ID);
        });
    }

    @Test
    void getByIdException() {
        Mockito.when(blendIngredientService.find(ID))
                .thenReturn(Try.failure(EXCEPTION));

        final Response response = controller.getById(ID);
        Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());

        Mockito.verify(blendIngredientService).find(ID);
    }

    @Test
    void getByBlendId() {
        Mockito.when(blendIngredientService.findByBlendExternalReference(EXTERNAL_REFERENCE))
                .thenReturn(Try.success(Collections.singletonList(SAMPLE_ENTITY)));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll(EXTERNAL_REFERENCE);
            Assertions.assertEquals(200, response.getStatus());

            Assertions.assertEquals(1, ((List<BlendIngredientGetResponse>) response.getEntity()).size());
            Assertions.assertEquals(SAMPLE_ENTITY.getId(), ((List<BlendIngredientGetResponse>) response.getEntity()).get(0).getId());

            Mockito.verify(blendIngredientService).findByBlendExternalReference(EXTERNAL_REFERENCE);
        });
    }

    @Test
    void getByBlendIdNotFound() {
        Mockito.when(blendIngredientService.findByBlendExternalReference(EXTERNAL_REFERENCE))
                .thenReturn(Try.success(Collections.emptyList()));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll(EXTERNAL_REFERENCE);
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(0, ((List<BlendIngredientGetResponse>) response.getEntity()).size());

            Mockito.verify(blendIngredientService).findByBlendExternalReference(EXTERNAL_REFERENCE);
        });

    }

    @Test
    void getByBlendIdException() {
        Mockito.when(blendIngredientService.findByBlendExternalReference(EXTERNAL_REFERENCE))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll(EXTERNAL_REFERENCE);
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(blendIngredientService).findByBlendExternalReference(EXTERNAL_REFERENCE);
    }

    @Test
    void save() {
        Mockito.when(blendIngredientService.save(BLEND_ID, INGREDIENT_ID))
                .thenReturn(Try.success(SAMPLE_ENTITY));
        Mockito.when(blendService.find(EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND_SAMPLE_ENTITY));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.save(EXTERNAL_REFERENCE, INGREDIENT_ID);
            Assertions.assertEquals(201, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getId(), ((BlendIngredientGetResponse) response.getEntity()).getId());

            Mockito.verify(blendIngredientService).save(BLEND_ID, INGREDIENT_ID);
            Mockito.verify(blendService).find(EXTERNAL_REFERENCE);
        });
    }

    @Test
    void saveException() {
        Mockito.when(blendIngredientService.save(BLEND_ID, INGREDIENT_ID))
                .thenReturn(Try.failure(EXCEPTION));
        Mockito.when(blendService.find(EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND_SAMPLE_ENTITY));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.save(EXTERNAL_REFERENCE, INGREDIENT_ID);
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(blendIngredientService).save(BLEND_ID, INGREDIENT_ID);
        Mockito.verify(blendService).find(EXTERNAL_REFERENCE);
    }

    @Test
    void update() {
        Mockito.when(blendIngredientService.update(Mockito.any(SAMPLE_ENTITY.getClass())))
                .thenReturn(Try.success(SAMPLE_ENTITY));
        Mockito.when(blendService.find(EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND_SAMPLE_ENTITY));
        Mockito.when(blendIngredientService.findByBlendId(BLEND_ID))
                .thenReturn(Try.success(Collections.singletonList(SAMPLE_ENTITY)));
        Mockito.when(blendIngredientService.saveHistory(Collections.singletonList(SAMPLE_ENTITY)))
                .thenReturn(Try.sequence(Collections.singletonList(Try.success(BLEND_ID))));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.update(EXTERNAL_REFERENCE, INGREDIENT_ID, SAMPLE_ENTITY_DTO);
            Assertions.assertEquals(202, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getId(), ((BlendIngredientGetResponse) response.getEntity()).getId());

            Mockito.verify(blendIngredientService).update(Mockito.any(SAMPLE_ENTITY.getClass()));
            Mockito.verify(blendService).find(EXTERNAL_REFERENCE);
            Mockito.verify(blendIngredientService).findByBlendId(BLEND_ID);
            Mockito.verify(blendIngredientService).saveHistory(Collections.singletonList(SAMPLE_ENTITY));
        });
    }

    @Test
    void updateException() {
        Mockito.when(blendIngredientService.update(Mockito.any(SAMPLE_ENTITY.getClass())))
                .thenReturn(Try.failure(EXCEPTION));
        Mockito.when(blendService.find(EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND_SAMPLE_ENTITY));
        Mockito.when(blendIngredientService.findByBlendId(BLEND_ID))
                .thenReturn(Try.success(Collections.singletonList(SAMPLE_ENTITY)));
        Mockito.when(blendIngredientService.saveHistory(Collections.singletonList(SAMPLE_ENTITY)))
                .thenReturn(Try.sequence(Collections.singletonList(Try.success(BLEND_ID))));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.update(EXTERNAL_REFERENCE, INGREDIENT_ID, SAMPLE_ENTITY_DTO);
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(blendIngredientService).update(Mockito.any(SAMPLE_ENTITY.getClass()));
        Mockito.verify(blendService).find(EXTERNAL_REFERENCE);
        Mockito.verify(blendIngredientService).findByBlendId(BLEND_ID);
        Mockito.verify(blendIngredientService).saveHistory(Collections.singletonList(SAMPLE_ENTITY));
    }

    @Test
    void delete() {
        Mockito.when(blendIngredientService.deleteIfNotInProcess(BLEND_ID, INGREDIENT_ID))
                .thenReturn(Try.success(null));
        Mockito.when(blendService.find(EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND_SAMPLE_ENTITY));
        Mockito.when(blendIngredientService.findByBlendId(BLEND_ID))
                .thenReturn(Try.success(Collections.singletonList(SAMPLE_ENTITY)));
        Mockito.when(blendIngredientService.saveHistory(Collections.singletonList(SAMPLE_ENTITY)))
                .thenReturn(Try.sequence(Collections.singletonList(Try.success(BLEND_ID))));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.delete(EXTERNAL_REFERENCE, INGREDIENT_ID);
            Assertions.assertEquals(202, response.getStatus());

            Mockito.verify(blendIngredientService).deleteIfNotInProcess(BLEND_ID, INGREDIENT_ID);
            Mockito.verify(blendService).find(EXTERNAL_REFERENCE);
            Mockito.verify(blendIngredientService).findByBlendId(BLEND_ID);
            Mockito.verify(blendIngredientService).saveHistory(Collections.singletonList(SAMPLE_ENTITY));
        });
    }

    @Test
    void deleteException() {
        Mockito.when(blendIngredientService.deleteIfNotInProcess(BLEND_ID, INGREDIENT_ID))
                .thenReturn(Try.failure(EXCEPTION));
        Mockito.when(blendService.find(EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND_SAMPLE_ENTITY));
        Mockito.when(blendIngredientService.findByBlendId(BLEND_ID))
                .thenReturn(Try.success(Collections.singletonList(SAMPLE_ENTITY)));
        Mockito.when(blendIngredientService.saveHistory(Collections.singletonList(SAMPLE_ENTITY)))
                .thenReturn(Try.sequence(Collections.singletonList(Try.success(BLEND_ID))));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.delete(EXTERNAL_REFERENCE, INGREDIENT_ID);
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(blendIngredientService).deleteIfNotInProcess(BLEND_ID, INGREDIENT_ID);
        Mockito.verify(blendService).find(EXTERNAL_REFERENCE);
        Mockito.verify(blendIngredientService).findByBlendId(BLEND_ID);
        Mockito.verify(blendIngredientService).saveHistory(Collections.singletonList(SAMPLE_ENTITY));
    }
}
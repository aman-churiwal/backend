package viteezy.controller;

import io.vavr.control.Try;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import viteezy.controller.dto.ingredient.IngredientPriceGetResponse;
import viteezy.domain.ingredient.IngredientPrice;
import viteezy.service.pricing.IngredientPriceService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class IngredientPriceControllerTest {

    private static final Long ID = 1L;
    private static final Long INGREDIENT_ID = 1L;
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final IngredientPrice SAMPLE_ENTITY = new IngredientPrice(ID, INGREDIENT_ID, BigDecimal.valueOf(300.00), DESCRIPTION, BigDecimal.valueOf(0.00), "EUR");

    private final IngredientPriceService service = Mockito.mock(IngredientPriceService.class);
    private IngredientPriceController controller;

    @BeforeEach
    void setUp() {
        Mockito.reset(service);
        controller = new IngredientPriceController(service);
    }

    @Test
    void findAll() {
        Mockito.when(service.findAllActive())
                .thenReturn(Try.success(Collections.singletonList(SAMPLE_ENTITY)));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll();
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(1, ((List<IngredientPriceGetResponse>) response.getEntity()).size());
            Assertions.assertEquals(SAMPLE_ENTITY.getAmount(), ((List<IngredientPriceGetResponse>) response.getEntity()).get(0).getAmount());

            Mockito.verify(service).findAllActive();
        });
    }

    @Test
    void findAllEmpty() {
        Mockito.when(service.findAllActive())
                .thenReturn(Try.success(Collections.emptyList()));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll();
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(0, ((List<IngredientPrice>) response.getEntity()).size());

            Mockito.verify(service).findAllActive();
        });

    }

    @Test
    void findAllException() {
        Mockito.when(service.findAllActive())
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.getAll();
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(service).findAllActive();
    }
}

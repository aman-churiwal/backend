package viteezy.controller;

import io.vavr.control.Try;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import viteezy.controller.dto.BlendGetResponse;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendStatus;
import viteezy.service.blend.BlendService;
import viteezy.service.blend.preview.QuizBlendAggregator;

import java.time.LocalDateTime;
import java.util.UUID;

class BlendControllerTest {

    private static final Long ID = 1L;
    private static final Long CUSTOMER_ID = 1L;
    private static final Long QUIZ_ID = 1L;
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final BlendStatus BLEND_STATUS = BlendStatus.CREATED;
    private static final UUID EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final Blend SAMPLE_ENTITY = new Blend(ID, BLEND_STATUS, EXTERNAL_REFERENCE, CUSTOMER_ID, QUIZ_ID, LocalDateTime.now(), LocalDateTime.now());

    private final BlendService service = Mockito.mock(BlendService.class);
    private final QuizBlendAggregator quizBlendAggregator = Mockito.mock(QuizBlendAggregator.class);
    private BlendController controller;

    @BeforeEach
    void setUp() {
        Mockito.reset(service);
        controller = new BlendController(service, quizBlendAggregator);
    }

    @Test
    void find() {
        Mockito.when(service.find(EXTERNAL_REFERENCE))
                .thenReturn(Try.success(SAMPLE_ENTITY));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.get(EXTERNAL_REFERENCE);
            Assertions.assertEquals(200, response.getStatus());
            Assertions.assertEquals(SAMPLE_ENTITY.getStatus().name(), ((BlendGetResponse) response.getEntity()).getStatus());

            Mockito.verify(service).find(EXTERNAL_REFERENCE);
        });
    }

    @Test
    void findException() {
        Mockito.when(service.find(EXTERNAL_REFERENCE))
                .thenReturn(Try.failure(EXCEPTION));

        Assertions.assertDoesNotThrow(() -> {
            Response response = controller.get(EXTERNAL_REFERENCE);
            Assertions.assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        });

        Mockito.verify(service).find(EXTERNAL_REFERENCE);
    }
}
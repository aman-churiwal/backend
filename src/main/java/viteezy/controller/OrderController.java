package viteezy.controller;

import io.swagger.annotations.Api;
import io.vavr.control.Try;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.domain.postnl.TrackTrace;
import viteezy.service.fulfilment.OrderService;
import viteezy.traits.ControllerResponseTrait;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Path("order")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Order")
public class OrderController implements ControllerResponseTrait {

    private final OrderService orderService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    public OrderController(@Qualifier("orderService") OrderService orderService) {
        this.orderService = orderService;
    }

    @GET
    @Path("/track-and-trace/{customerExternalReference}")
    public Response get(@PathParam("customerExternalReference") UUID customerExternalReference) {
        final Try<Response> foldedTry = orderService.findTrackAndTraceByByCustomerExternalReference(customerExternalReference)
                .map(foldOptional());
        return getResponseFromTry(foldedTry, LOGGER);
    }

    private Function<Optional<TrackTrace>, Response> foldOptional() {
        return optional -> optional
                .map(this::getOkResponseWithEntity)
                .orElseThrow();
    }
}


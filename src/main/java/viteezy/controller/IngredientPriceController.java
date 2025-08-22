package viteezy.controller;

import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Try;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.controller.dto.ingredient.IngredientPriceGetResponse;
import viteezy.domain.ingredient.IngredientPrice;
import viteezy.service.pricing.IngredientPriceService;
import viteezy.traits.ControllerResponseTrait;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("ingredients")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Ingredient Price")
public class IngredientPriceController implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientPriceController.class);

    private final IngredientPriceService service;

    @Autowired
    public IngredientPriceController(
            @Qualifier("ingredientPriceService") IngredientPriceService service
    ) {
        this.service = service;
    }

    @GET
    @Path("/prices")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = IngredientPriceGetResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getAll() {
        final Try<Response> foldedTry = service
                .findAllActive()
                .map(foldList());
        return getResponseFromTry(foldedTry, LOGGER);
    }

    private Function<List<IngredientPrice>, Response> foldList() {
        return list -> {
            final List<IngredientPriceGetResponse> dtoList = list.stream()
                    .map(IngredientPriceGetResponse::from)
                    .collect(Collectors.toList());
            return getOkResponseWithEntity(dtoList);
        };
    }
}

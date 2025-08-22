package viteezy.controller;

import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.controller.dto.BlendIngredientGetResponse;
import viteezy.controller.dto.BlendIngredientPostRequest;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendIngredient;
import viteezy.service.blend.BlendIngredientService;
import viteezy.service.blend.BlendService;
import viteezy.traits.ControllerResponseTrait;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("blends")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Blend's ingredients")
public class BlendIngredientController implements ControllerResponseTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlendIngredientController.class);

    private final BlendIngredientService blendIngredientService;
    private final BlendService blendService;

    @Autowired
    public BlendIngredientController(
            @Qualifier("blendIngredientService") BlendIngredientService blendIngredientService,
            @Qualifier("blendService") BlendService blendService
    ) {
        this.blendIngredientService = blendIngredientService;
        this.blendService = blendService;
    }

    @GET
    @Path("/ingredients/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = BlendIngredientGetResponse.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getById(@PathParam("id") Long id) {
        final Try<Response> foldedTry = blendIngredientService
                .find(id)
                .map(BlendIngredientGetResponse::from)
                .map(this::getOkResponseWithEntity);
        return getResponseFromTry(foldedTry, LOGGER);
    }

    @GET
    @Path("/{blendExternalReference}/ingredients")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "", response = BlendIngredientGetResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response getAll(@PathParam("blendExternalReference") UUID blendExternalReference) {
        final Try<Response> foldedTry = blendIngredientService.findByBlendExternalReference(blendExternalReference)
                .map(foldList());
        return getResponseFromTry(foldedTry, LOGGER);
    }

    @POST
    @Path("/{blendExternalReference}/ingredients/{ingredientId}")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "", response = BlendIngredientGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response save(
            @PathParam("blendExternalReference") UUID blendExternalReference, @PathParam("ingredientId") Long ingredientId
    ) {
        final Try<Response> foldedTry = blendService.find(blendExternalReference)
                .flatMap(blend -> blendIngredientService.save(blend.getId(), ingredientId))
                .map(BlendIngredientGetResponse::from)
                .map(getCreatedEntityResponse());
        return getResponseFromTry(foldedTry, LOGGER);
    }

    @PUT
    @Path("/{blendExternalReference}/ingredients/{ingredientId}")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "", response = BlendIngredientGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response update(
            @PathParam("blendExternalReference") UUID blendExternalReference, @PathParam("ingredientId") Long ingredientId,
            @NotNull @Valid BlendIngredientPostRequest blendIngredientPostRequest
    ) {
        final Try<Response> foldedTry = blendService.find(blendExternalReference)
                .flatMap(this::processHistory)
                .map(blend -> buildBlendIngredient(blend.getId(), ingredientId, blendIngredientPostRequest))
                .flatMap(blendIngredientService::update)
                .map(BlendIngredientGetResponse::from)
                .map(getUpdatedEntityResponse());
        return getResponseFromTry(foldedTry, LOGGER);
    }

    @DELETE
    @Path("/{blendExternalReference}/ingredients/{ingredientId}")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "", response = BlendIngredientGetResponse.class),
            @ApiResponse(code = 404, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 400, message = "", response = ErrorMessage.class),
            @ApiResponse(code = 500, message = "", response = ErrorMessage.class)
    })
    public Response delete(
            @PathParam("blendExternalReference") UUID blendExternalReference, @PathParam("ingredientId") Long ingredientId
    ) {
        final Try<Response> foldedTry = blendService.find(blendExternalReference)
                .flatMap(this::processHistory)
                .flatMap(blend -> blendIngredientService.deleteIfNotInProcess(blend.getId(), ingredientId))
                .map(getUpdatedEntityResponse());
        return getResponseFromTry(foldedTry, LOGGER);
    }

    private Try<Blend> processHistory(Blend blend) {
        return blendIngredientService.findByBlendId(blend.getId())
                .flatMap(blendIngredientService::saveHistory)
                .map(__ -> blend);
    }

    private BlendIngredient buildBlendIngredient(
            Long blendId, Long ingredientId, BlendIngredientPostRequest blendIngredientPostRequest
    ) {
        final BigDecimal amount = blendIngredientPostRequest.getAmount();
        final String isUnit = blendIngredientPostRequest.getIsUnit();
        return BlendIngredient.build(ingredientId, blendId, amount, isUnit, null);
    }

    private Function<List<BlendIngredient>, Response> foldList() {
        return list -> {
            final List<BlendIngredientGetResponse> dtoList = list.stream()
                    .map(BlendIngredientGetResponse::from)
                    .collect(Collectors.toList());
            return getOkResponseWithEntity(dtoList);
        };
    }
}
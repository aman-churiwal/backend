package viteezy.controller.dashboard;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.vavr.control.Either;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.controller.dto.dashboard.JwtResponse;
import viteezy.controller.dto.dashboard.LoginPostRequest;
import viteezy.controller.dto.dashboard.SignUpRequest;
import viteezy.service.dashboard.UserService;
import viteezy.traits.ControllerResponseTrait;

import java.util.Optional;

@Path("dashboard/users")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "user")
public class UserController implements ControllerResponseTrait {

    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(@Qualifier("userService") UserService userService) {
        this.userService = userService;
    }

    @GET
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/all")
    public Response findAll() {
        return userService.findAll()
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @GET
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) throws Throwable {
        return getResponse(userService.find(id), Response.Status.OK, Response.Status.NOT_FOUND);
    }

    @POST
    @Timed
    @RolesAllowed("ADMIN")
    @Path("/register")
    public Response upsert(@NotNull @Valid SignUpRequest signUpRequest) throws Throwable {
        return getResponse(userService.upsert(signUpRequest), Response.Status.CREATED, Response.Status.BAD_REQUEST);
    }

    @POST
    @Timed
    @Path("/login")
    public Response login(@NotNull @Valid LoginPostRequest loginRequest) throws Throwable {
        final String email = loginRequest.getEmail();
        final String password = loginRequest.getPassword();
        return userService.login(email, password)
                .map(this::getResponse)
                .getOrElseThrow(throwable -> throwable);
    }

    private Response getResponse(JwtResponse jwtResponse) {
        return Response
                .status(Response.Status.OK)
                .entity(jwtResponse)
                .build();
    }

    private <T> Response getResponse(Either<Throwable, Optional<T>> currentEither, Response.StatusType goodResponse, Response.StatusType badResponse) throws Throwable {
        if (currentEither.isRight() && currentEither.get().isPresent()) {
            return Response.status(goodResponse).entity(currentEither.get().get()).build();
        } else if (currentEither.isRight() && currentEither.get().isEmpty()) {
            return Response.status(badResponse).build();
        } else throw currentEither.getLeft();
    }
}
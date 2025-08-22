package viteezy.controller;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.service.LoginService;
import viteezy.traits.ControllerResponseTrait;

@Path("login")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Login")
public class LoginController implements ControllerResponseTrait {

    private final LoginService loginService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    public LoginController(@Qualifier("loginService") LoginService loginService) {
        this.loginService = loginService;
    }

    @POST
    @Timed
    public Response sendAuthenticationMail(@QueryParam("email") String email) {
        return loginService.sendAuthenticationMail(email)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @GET
    @Timed
    @Path("/{token}")
    public Response authenticate(@PathParam("token") String token,
                                 @QueryParam("email") String email) {
        return Response.seeOther(loginService.authenticate(email, token)).build();
    }
}
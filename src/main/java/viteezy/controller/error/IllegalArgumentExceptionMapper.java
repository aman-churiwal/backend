package viteezy.controller.error;

import io.dropwizard.jersey.errors.ErrorMessage;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;

@Provider
@Component
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        Response.StatusType type = Response.Status.BAD_REQUEST;

        ErrorMessage errorMessage = buildErrorMessage(exception, type);
        return Response.status(type.getStatusCode())
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private ErrorMessage buildErrorMessage(Throwable exception, Response.StatusType type) {
        return new ErrorMessage(type.getStatusCode(), exception.getMessage());
    }
}

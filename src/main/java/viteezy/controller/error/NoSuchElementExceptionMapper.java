package viteezy.controller.error;

import io.dropwizard.jersey.errors.ErrorMessage;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Provider
@Component
public class NoSuchElementExceptionMapper implements ExceptionMapper<NoSuchElementException> {

    @Override
    public Response toResponse(NoSuchElementException exception) {
        Response.StatusType type = Response.Status.NOT_FOUND;

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

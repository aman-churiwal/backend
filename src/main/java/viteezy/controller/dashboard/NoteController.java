package viteezy.controller.dashboard;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import viteezy.controller.dto.dashboard.NoteRequest;
import viteezy.domain.dashboard.User;
import viteezy.service.dashboard.NoteService;
import viteezy.traits.ControllerResponseTrait;

import java.util.UUID;

@Path("dashboard/notes")
@Produces(MediaType.APPLICATION_JSON)
@Component
@Api(tags = "Dashboard")
public class NoteController implements ControllerResponseTrait {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

    private final NoteService noteService;

    @Autowired
    public NoteController(
            @Qualifier("noteService") NoteService noteService
    ) {
        this.noteService = noteService;
    }

    @GET
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/customer/{customerExternalReference}")
    public Response getNotesByCustomerExternalReference(@PathParam("customerExternalReference") UUID customerExternalReference) {
        return noteService.findByCustomerExternalReference(customerExternalReference)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @POST
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/customer/{customerExternalReference}")
    public Response createNoteByCustomerExternalReference(@Auth User user,
                                                          @PathParam("customerExternalReference") UUID customerExternalReference,
                                                          NoteRequest noteRequest) {
        return noteService.create(user.getId(), customerExternalReference, noteRequest.getMessage())
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @PATCH
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/{noteId}")
    public Response updateNote(@PathParam("noteId") Long noteId, NoteRequest noteRequest) {
        return noteService.update(noteId, noteRequest.getMessage())
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }

    @DELETE
    @Timed
    @RolesAllowed({"USER","ADMIN"})
    @Path("/{noteId}")
    public Response deleteNote(@PathParam("noteId") Long noteId) {
        return noteService.delete(noteId)
                .map(this::getOkResponseWithEntity)
                .getOrElseGet(getFailureResponse(LOGGER));
    }
}

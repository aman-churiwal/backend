package viteezy.service.dashboard;

import io.vavr.control.Try;
import viteezy.domain.dashboard.Note;

import java.util.List;
import java.util.UUID;

public interface NoteService {

    Try<Void> delete(Long noteId);

    Try<List<Note>> findByCustomerExternalReference(UUID customerExternalReference);

    Try<Note> create(Long userId, UUID customerExternalReference, String message);

    Try<Note> update(Long noteId, String message);
}

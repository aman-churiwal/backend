package viteezy.db.dashboard;

import io.vavr.control.Try;
import viteezy.domain.dashboard.Note;

import java.util.List;

public interface NoteRepository {

    Try<Note> find(Long id);

    Try<List<Note>> findByCustomerId(Long customerId);

    Try<Note> create(Note note);

    Try<Note> update(Note note);

    Try<Void> delete(Long noteId);
}

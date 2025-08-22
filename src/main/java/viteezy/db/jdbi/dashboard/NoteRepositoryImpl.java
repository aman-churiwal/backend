package viteezy.db.jdbi.dashboard;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.db.dashboard.NoteRepository;
import viteezy.domain.dashboard.Note;

import java.util.List;

public class NoteRepositoryImpl implements NoteRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteRepository.class);

    private static final String SELECT_ALL = "SELECT * FROM notes ";
    private static final String INSERT_QUERY = "" +
            "INSERT INTO " +
            "notes (id, from_id, customer_id, message) " +
            "VALUES (:id, :fromId, :customerId, :message) ";
    private static final String UPDATE_MESSAGE_QUERY = "" +
            "UPDATE notes SET message = :message, modification_timestamp = NOW() " +
            "WHERE id = :id";
    private static final String DELETE_BY_ID = "DELETE FROM notes WHERE id = :id";
    private final Jdbi jdbi;

    public NoteRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Note> find(Long id) {
        final HandleCallback<Note, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Note.class)
                .one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Note>> findByCustomerId(Long customerId) {
        final HandleCallback<List<Note>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE customer_id = :customerId ORDER BY modification_timestamp DESC")
                .bind("customerId", customerId)
                .mapTo(Note.class)
                .list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Note> create(Note note) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bind("id", note.getId())
                .bind("fromId", note.getFromId())
                .bind("customerId", note.getCustomerId())
                .bind("message", note.getMessage())
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }

    @Override
    public Try<Note> update(Note note) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_MESSAGE_QUERY)
                .bind("id", note.getId())
                .bind("message", note.getMessage())
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(__ -> find(note.getId()));
    }

    @Override
    public Try<Void> delete(Long noteId) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(DELETE_BY_ID)
                .bind("id", noteId)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(integer -> Try.success(null));
    }
}

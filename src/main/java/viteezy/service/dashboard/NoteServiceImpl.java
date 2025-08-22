package viteezy.service.dashboard;

import io.vavr.control.Try;
import viteezy.db.dashboard.NoteRepository;
import viteezy.domain.dashboard.Note;
import viteezy.service.CustomerService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class NoteServiceImpl implements NoteService{

    private final NoteRepository noteRepository;
    private final CustomerService customerService;

    protected NoteServiceImpl(NoteRepository noteRepository, CustomerService customerService) {
        this.noteRepository = noteRepository;
        this.customerService = customerService;
    }

    @Override
    public Try<Void> delete(Long noteId) {
        return noteRepository.delete(noteId);
    }

    @Override
    public Try<List<Note>> findByCustomerExternalReference(UUID customerExternalReference) {
        return customerService.find(customerExternalReference)
                .flatMap(customer -> noteRepository.findByCustomerId(customer.getId()));
    }

    @Override
    public Try<Note> create(Long userId, UUID customerExternalReference, String message) {
        return customerService.find(customerExternalReference)
                .flatMap(customer -> noteRepository.create(buildNote(userId, customer.getId(), message)));
    }

    @Override
    public Try<Note> update(Long noteId, String message) {
        return noteRepository.find(noteId)
                .flatMap(note -> noteRepository.update(buildNoteWithMessage(note, message)));
    }

    private Note buildNote(Long userId, Long customerId, String message) {
        return new Note(null, userId, customerId, message, LocalDateTime.now(), LocalDateTime.now());
    }

    private Note buildNoteWithMessage(Note note, String message) {
        return new Note(note.getId(), note.getFromId(), note.getCustomerId(), message, note.getCreationTimestamp(), LocalDateTime.now());
    }
}

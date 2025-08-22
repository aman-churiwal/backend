package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.ContentRepository;
import viteezy.domain.WebsiteContent;

public class ContentRepositoryImpl implements ContentRepository {

    private static final String SELECT_ALL = "SELECT * FROM website_content ";

    private final Jdbi jdbi;

    public ContentRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<WebsiteContent> findByCode(String code) {
        final HandleCallback<WebsiteContent, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE code = :code")
                .bind("code", code)
                .mapTo(WebsiteContent.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }
}

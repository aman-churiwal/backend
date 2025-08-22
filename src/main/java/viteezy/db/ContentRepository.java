package viteezy.db;

import io.vavr.control.Try;
import viteezy.domain.WebsiteContent;

public interface ContentRepository {

    Try<WebsiteContent> findByCode(String code);
}

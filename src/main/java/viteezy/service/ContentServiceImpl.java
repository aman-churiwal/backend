package viteezy.service;

import io.vavr.control.Try;
import viteezy.db.ContentRepository;
import viteezy.domain.WebsiteContent;

public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;

    protected ContentServiceImpl(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Override
    public Try<WebsiteContent> findByCode(String code) {
        return contentRepository.findByCode(code);
    }
}

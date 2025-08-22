package viteezy.service.tasks;

import com.google.common.base.CaseFormat;
import org.springframework.stereotype.Component;
import viteezy.service.fulfilment.XMLWriterService;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Task
@Component
public class PostNlFulfilmentRequest extends io.dropwizard.servlets.tasks.Task {

    private final XMLWriterService xmlWriterService;

    protected PostNlFulfilmentRequest(XMLWriterService xmlWriterService) {
        super(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, PostNlFulfilmentRequest.class.getSimpleName()));
        this.xmlWriterService = xmlWriterService;
    }

    @Override
    public void execute(Map<String, List<String>> map, PrintWriter printWriter) throws Exception {
        xmlWriterService.createDeliveryOrderMessages();
    }
}

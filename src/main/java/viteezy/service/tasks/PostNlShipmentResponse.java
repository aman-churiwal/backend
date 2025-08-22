package viteezy.service.tasks;

import com.google.common.base.CaseFormat;
import org.springframework.stereotype.Component;
import viteezy.service.fulfilment.XMLReaderService;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Task
@Component
public class PostNlShipmentResponse extends io.dropwizard.servlets.tasks.Task {

    private final XMLReaderService xmlReaderService;

    protected PostNlShipmentResponse(XMLReaderService xmlReaderService) {
        super(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, PostNlShipmentResponse.class.getSimpleName()));
        this.xmlReaderService = xmlReaderService;
    }

    @Override
    public void execute(Map<String, List<String>> map, PrintWriter printWriter) throws Exception {
        xmlReaderService.updateDeliveryOrderMessages();
    }
}

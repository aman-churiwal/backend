package viteezy.service.tasks;

import com.google.common.base.CaseFormat;
import org.springframework.stereotype.Component;
import viteezy.service.fulfilment.CSVWriterService;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Task
@Component
public class PharmacistRequest extends io.dropwizard.servlets.tasks.Task {

    private final CSVWriterService csvWriterService;

    protected PharmacistRequest(CSVWriterService csvWriterService) {
        super(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, PharmacistRequest.class.getSimpleName()));
        this.csvWriterService = csvWriterService;
    }

    @Override
    public void execute(Map<String, List<String>> map, PrintWriter printWriter) {
        csvWriterService.createPharmacistRequest();
    }
}

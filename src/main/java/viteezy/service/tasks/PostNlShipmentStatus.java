package viteezy.service.tasks;

import com.google.common.base.CaseFormat;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import viteezy.gateways.postnl.PostNlService;
import viteezy.service.fulfilment.OrderService;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Task
@Component
public class PostNlShipmentStatus extends io.dropwizard.servlets.tasks.Task {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostNlShipmentStatus.class);
    private final OrderService orderService;
    private final PostNlService postNlService;

    protected PostNlShipmentStatus(OrderService orderService, PostNlService postNlService) {
        super(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, PostNlShipmentStatus.class.getSimpleName()));
        this.orderService = orderService;
        this.postNlService = postNlService;
    }

    @Override
    public void execute(Map<String, List<String>> map, PrintWriter printWriter) throws Exception {
        postNlService.getShippingStatuses()
                .flatMap(orderService::updateByTrackTrace)
                .onFailure(peekException());
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}

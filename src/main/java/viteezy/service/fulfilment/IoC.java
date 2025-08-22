package viteezy.service.fulfilment;

import io.vavr.control.Try;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import viteezy.configuration.FulfilmentConfiguration;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.db.OrderRepository;
import viteezy.db.PharmacistOrderRepository;
import viteezy.gateways.infobip.InfobipService;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.service.CustomerService;
import viteezy.service.IngredientService;
import viteezy.service.IngredientUnitService;
import viteezy.service.blend.BlendIngredientService;
import viteezy.service.mail.EmailService;

@Configuration("fulfilmentIoC")
public class IoC {

    private final FulfilmentConfiguration fulfilmentConfiguration;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IoC(
            ViteezyConfiguration viteezyConfiguration
    ) {
        this.fulfilmentConfiguration = viteezyConfiguration.getFulfilmentConfiguration();
    }

    @Bean("CSVWriterService")
    public CSVWriterService csvWriterService(OrderService orderService, PharmacistOrderService pharmacistOrderService,
                                             BlendIngredientService blendIngredientService, EmailService emailService,
                                             IngredientUnitService ingredientUnitService) {
        return new CSVWriterServiceImpl(orderService, pharmacistOrderService, blendIngredientService, emailService,
                ingredientUnitService);
    }

    @Bean("XMLWriterService")
    public XMLWriterService xmlWriterService(
            BlendIngredientService blendIngredientService, OrderService orderService) {
        return new XMLWriterServiceImpl(blendIngredientService, orderService, fulfilmentConfiguration);
    }

    @Bean("XMLReaderService")
    public XMLReaderService xmlReaderService(OrderService orderService) {
        return new XMLReaderServiceImpl(fulfilmentConfiguration, orderService);
    }

    @Bean("orderService")
    public OrderService orderService(OrderRepository orderRepository, CustomerService customerService,
                                     InfobipService infobipService, KlaviyoService klaviyoService) {
        return new OrderServiceImpl(orderRepository, customerService, infobipService, klaviyoService);
    }

    @Bean("pharmacistOrderService")
    public PharmacistOrderService pharmacistOrderService(PharmacistOrderRepository pharmacistOrderRepository) {
        return new PharmacistOrderServiceImpl(pharmacistOrderRepository);
    }

    @Bean("sshClient")
    public Try<SSHClient> sshClient() {
        return Try.of(() -> {
            SSHClient client = new SSHClient();
            client.addHostKeyVerifier(new PromiscuousVerifier());
            client.connect(fulfilmentConfiguration.getSftpRemoteHost());
            client.authPublickey(fulfilmentConfiguration.getSftpUsername(), client.loadKeys(fulfilmentConfiguration.getSftpPrivateKeyFilename()));
            return client;
        });
    }
}

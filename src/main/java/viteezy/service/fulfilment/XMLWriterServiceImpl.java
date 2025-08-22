package viteezy.service.fulfilment;

import com.google.common.base.Throwables;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.configuration.FulfilmentConfiguration;
import viteezy.domain.fulfilment.Order;
import viteezy.domain.fulfilment.xml.DeliveryOrder;
import viteezy.domain.fulfilment.xml.DeliveryOrderLine;
import viteezy.domain.fulfilment.xml.DeliveryOrderMessage;
import viteezy.domain.ingredient.Ingredient;
import viteezy.domain.fulfilment.OrderStatus;
import viteezy.service.blend.BlendIngredientService;
import viteezy.traits.EnforcePresenceTrait;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class XMLWriterServiceImpl implements XMLWriterService, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLWriterService.class);

    private final static String XML_FOLDER = "/data";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final String SHIPPING_NL = "03085";
    private static final String SHIPPING_BE = "04946";
    private static final String SHIPMENT_TYPE = "Commercial Goods";
    private static final String LANGUAGE = "NL";

    private final BlendIngredientService blendIngredientService;
    private final OrderService orderService;
    private final FulfilmentConfiguration fulfilmentConfiguration;
    private final Marshaller jaxbMarshaller;

    public XMLWriterServiceImpl(BlendIngredientService blendIngredientService, OrderService orderService,
                                FulfilmentConfiguration fulfilmentConfiguration) {
        this.blendIngredientService = blendIngredientService;
        this.orderService = orderService;
        this.fulfilmentConfiguration = fulfilmentConfiguration;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DeliveryOrderMessage.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            final File xmlFile = new File(XML_FOLDER + "/test.xml");
            DeliveryOrderMessage deliveryOrderMessage = new DeliveryOrderMessage(0L, "test", "test", Collections.emptyList());
            jaxbMarshaller.marshal(deliveryOrderMessage, xmlFile);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Try<List<File>> createDeliveryOrderMessage(UUID orderId) {
        return orderService.find(orderId)
                .flatMap(order -> getDeliveryOrderMessages(Collections.singletonList(order)))
                .flatMap(this::writeXMLFiles)
                .flatMap(this::uploadFiles)
                .onFailure(peekException());
    }

    @Override
    public Try<List<File>> createDeliveryOrderMessages() {
        return createDirectory()
                .flatMap(__ -> findOrders())
                .flatMap(this::getDeliveryOrderMessages)
                .flatMap(this::writeXMLFiles)
                .flatMap(this::uploadFiles)
                .onFailure(peekException());
    }

    private Try<List<Order>> findOrders() {
        return orderService.findByStatus(OrderStatus.SHIPPED_TO_PHARMACIST)
                .peek(orders -> LOGGER.info("Orders: {}", orders.size()));
    }

    private Try<Seq<DeliveryOrderMessage>> getDeliveryOrderMessages(List<Order> orders) {
        return Try.sequence(orders.stream().map(this::getDeliveryOrderMessage).collect(Collectors.toList()));
    }

    private Try<DeliveryOrderMessage> getDeliveryOrderMessage(Order order) {
        return getDeliveryOrder(order)
                .map(deliveryOrder -> new DeliveryOrderMessage(order.getId(), LocalDate.now().format(DATE_FORMATTER), LocalTime.now().format(TIME_FORMATTER),
                        Collections.singletonList(deliveryOrder)));
    }

    private Try<DeliveryOrder> getDeliveryOrder(Order order) {
        return getDeliveryOrderLine(order)
                .map(deliveryOrderLines -> buildDeliveryOrder(order, deliveryOrderLines));
    }

    private DeliveryOrder buildDeliveryOrder(Order order, List<DeliveryOrderLine> deliveryOrderLines) {
        return new DeliveryOrder(order.getOrderNumber(), order.getId().toString(),
                order.getCreated().format(DATE_FORMATTER), order.getCreated().format(TIME_FORMATTER),
                order.getCustomerId().toString(), false, order.getShipToFirstName(),
                order.getShipToLastName(), order.getShipToStreet(), order.getShipToHouseNo(), order.getShipToAnnex(),
                order.getShipToPostalCode(), order.getShipToCity(), order.getShipToCountryCode(), order.getShipToPhone(),
                order.getShipToEmail(), LANGUAGE, getShippingAgentCode(order.getShipToCountryCode()), SHIPMENT_TYPE,
                deliveryOrderLines);
    }

    private String getShippingAgentCode(String shipToCountryCode) {
        switch (shipToCountryCode) {
            case "NL": return SHIPPING_NL;
            case "BE": return SHIPPING_BE;
            default: return "";
        }
    }

    private Try<List<DeliveryOrderLine>> getDeliveryOrderLine(Order order) {
        return blendIngredientService.getIngredients(order.getBlendId())
                .map(ingredients -> getDeliveryOrderLines(order, ingredients))
                .onFailure(peekException());
    }

    private List<DeliveryOrderLine> getDeliveryOrderLines(Order order, Seq<Ingredient> ingredients) {
        List<DeliveryOrderLine> lines = new ArrayList<>();
        String packs = String.valueOf(order.getRecurringMonths() * 30);
        final int sleepFormula = ingredients.filter(ingredient -> ingredient.getCode().equals("sleep-formula")).size();
        if (ingredients.size() > sleepFormula) {
            lines.add(new DeliveryOrderLine("VIT" + packs, "Vitamine packs", 1));
        }
        if (sleepFormula == 1) {
            lines.add(new DeliveryOrderLine("SLP" + packs, "Sleep formula bottle", 1));
        }
        ingredients.filter(ingredient -> ingredient.getSku() != null)
                .forEach(ingredient -> {
                    lines.add(new DeliveryOrderLine(ingredient.getSku(), ingredient.getDescription(), 1));
                    blendIngredientService.delete(order.getBlendId(), ingredient.getId());
                });
        return lines;
    }

    private Try<Void> createDirectory() {
        return Try.run(() -> Files.createDirectories(Paths.get(XML_FOLDER)));
    }

    private Try<List<File>> writeXMLFiles(Seq<DeliveryOrderMessage> deliveryOrderMessages) {
        return Try.of(() -> {
            List<File> files = new ArrayList<>();
            deliveryOrderMessages.forEach(deliveryOrderMessage -> {
                final File xmlFile = new File(XML_FOLDER + "/" + deliveryOrderMessage.getMessageNo() + ".xml");
                final DeliveryOrder deliveryOrder = deliveryOrderMessage.getDeliveryOrders().get(0);
                Try.run(() -> jaxbMarshaller.marshal(deliveryOrderMessage, xmlFile))
                        .filter(__ -> !deliveryOrder.getDeliveryOrderLines().isEmpty())
                        .onSuccess(__ -> files.add(xmlFile))
                        .onFailure(__ -> orderService.find(deliveryOrder.getOrderNo())
                                .flatMap(optionalToNarrowedTry())
                                .peek(order -> orderService.update(buildOrderWithError(order)))
                        );
            });
            return files;
        });
    }

    private Try<List<File>> uploadFiles(List<File> files)  {
        List<File> uploaded = new ArrayList<>();
        return setupSshj().flatMap(sshClient -> Try.of(() -> {
            SFTPClient sftpClient = sshClient.newSFTPClient();
            final String destDirectory = "/home/" + fulfilmentConfiguration.getSftpUsername() + "/Order/";

            files.forEach(file -> Try.run(() -> sftpClient.put(new FileSystemFile(file), destDirectory + file.getName()))
                    .onSuccess(__ -> uploaded.add(file))
                    .onSuccess(__ -> orderService.update(file.getName(), OrderStatus.SHIPPED_TO_POSTNL))
                    .onFailure(peekException()));

            sftpClient.close();
            sshClient.disconnect();
            return uploaded;
        }));
    }

    private Order buildOrderWithError(Order order) {
        return new Order(order.getId(), order.getExternalReference(), order.getOrderNumber(), order.getPaymentId(),
                order.getSequenceType(), order.getPaymentPlanId(), order.getBlendId(), order.getCustomerId(),
                order.getRecurringMonths(), order.getShipToFirstName(), order.getShipToLastName(),
                order.getShipToStreet(), order.getShipToHouseNo(), order.getShipToAnnex(), order.getShipToPostalCode(),
                order.getShipToCity(), order.getShipToCountryCode(), order.getShipToPhone(), order.getShipToEmail(),
                order.getReferralCode(), order.getTrackTraceCode(), order.getPharmacistOrderNumber(), OrderStatus.ERROR,
                order.getCreated(), order.getShipped(), order.getLastModified());
    }

    private Try<SSHClient> setupSshj() {
        return Try.of(() -> {
            SSHClient client = new SSHClient();
            client.addHostKeyVerifier(new PromiscuousVerifier());
            client.connect(fulfilmentConfiguration.getSftpRemoteHost());
            client.authPublickey(fulfilmentConfiguration.getSftpUsername(), client.loadKeys(fulfilmentConfiguration.getSftpPrivateKeyFilename()));
            return client;
        });
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}
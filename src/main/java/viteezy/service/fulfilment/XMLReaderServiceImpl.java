package viteezy.service.fulfilment;

import com.google.common.base.Throwables;
import io.vavr.control.Try;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteFile;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.configuration.FulfilmentConfiguration;
import viteezy.domain.fulfilment.Order;
import viteezy.domain.fulfilment.xml.DeliveryOrderResponse;
import viteezy.domain.fulfilment.OrderStatus;
import viteezy.traits.EnforcePresenceTrait;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

public class XMLReaderServiceImpl implements XMLReaderService, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(XMLReaderService.class);

    private final FulfilmentConfiguration fulfilmentConfiguration;
    private final OrderService orderService;
    private final Unmarshaller jaxbUnmarshaller;

    protected XMLReaderServiceImpl(FulfilmentConfiguration fulfilmentConfiguration, OrderService orderService) {
        this.fulfilmentConfiguration = fulfilmentConfiguration;
        this.orderService = orderService;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DeliveryOrderResponse.class);
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Try<Void> updateDeliveryOrderMessages() {
        return setupSshj().flatMap(sshClient -> Try.run(() -> {
            SFTPClient sftpClient = sshClient.newSFTPClient();
            final String destDirectory = "/home/" + fulfilmentConfiguration.getSftpUsername() + "/Shipment/";

            List<RemoteResourceInfo> shipmentResponses = sftpClient.ls(destDirectory);
            shipmentResponses.stream()
                    .filter(remoteResourceInfo -> remoteResourceInfo.getName().endsWith(".xml"))
                    .forEach(remoteResourceInfo -> Try.run(() -> {
                final RemoteFile remoteFile = sftpClient.getSFTPEngine().open(remoteResourceInfo.getPath());
                final InputStream inputStream = remoteFile.new RemoteFileInputStream(0);
                final DeliveryOrderResponse deliveryOrderResponse = (DeliveryOrderResponse) jaxbUnmarshaller.unmarshal(inputStream);
                final String orderNumber = deliveryOrderResponse.getOrderStatus().getOrderNo();
                final String trackAndTraceCode = deliveryOrderResponse.getOrderStatus().getTrackAndTraceCode();
                orderService.find(orderNumber)
                        .flatMap(optionalToNarrowedTry())
                        .peek(order -> orderService.update(buildOrder(order, trackAndTraceCode)))
                        .peek(__ -> Try.run(() -> sftpClient.rm(remoteResourceInfo.getPath())))
                        .onFailure(logExceptionAndShipmentResponse(remoteResourceInfo.getName(), orderNumber));

            }).onFailure(peekException()));

            sftpClient.close();
            sshClient.disconnect();
        })).onFailure(peekException());
    }

    private Order buildOrder(Order order, String trackAndTraceCode) {
        return new Order(order.getId(), order.getExternalReference(), order.getOrderNumber(), order.getPaymentId(),
                order.getSequenceType(), order.getPaymentPlanId(), order.getBlendId(), order.getCustomerId(),
                order.getRecurringMonths(), order.getShipToFirstName(), order.getShipToLastName(),
                order.getShipToStreet(), order.getShipToHouseNo(), order.getShipToAnnex(), order.getShipToPostalCode(),
                order.getShipToCity(), order.getShipToCountryCode(), order.getShipToPhone(), order.getShipToEmail(),
                order.getReferralCode(), trackAndTraceCode, order.getPharmacistOrderNumber(),
                OrderStatus.SHIPPED_TO_CUSTOMER, order.getCreated(), order.getShipped(), order.getLastModified());
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

    private Consumer<Throwable> logExceptionAndShipmentResponse(String fileName, String orderNumber) {
        return throwable -> LOGGER.error("PostNL shipment response no order found file name {} order number {} exception {}", fileName, orderNumber, Throwables.getStackTraceAsString(throwable));
    }
}

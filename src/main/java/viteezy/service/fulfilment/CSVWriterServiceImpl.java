package viteezy.service.fulfilment;

import be.woutschoovaerts.mollie.data.payment.SequenceType;
import com.google.common.base.Throwables;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.domain.blend.BlendIngredient;
import viteezy.domain.fulfilment.Order;
import viteezy.domain.fulfilment.PharmacistOrder;
import viteezy.domain.ingredient.UnitCode;
import viteezy.domain.fulfilment.PharmacistOrderLine;
import viteezy.domain.ingredient.IngredientUnit;
import viteezy.domain.fulfilment.OrderStatus;
import viteezy.domain.fulfilment.PharmacistOrderStatus;
import viteezy.service.IngredientService;
import viteezy.service.IngredientUnitService;
import viteezy.service.blend.BlendIngredientService;
import viteezy.service.mail.EmailService;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVWriterServiceImpl implements CSVWriterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVWriterService.class);

    private final OrderService orderService;
    private final PharmacistOrderService pharmacistOrderService;
    private final BlendIngredientService blendIngredientService;
    private final EmailService emailService;
    private final IngredientUnitService ingredientUnitService;

    private final static String CSV_FOLDER = "/data/csv";

    private final Long SLEEP_INGREDIENT_ID = 28L;
    private final String CSV_FILE_EXTENSION = ".csv";
    private final Integer ONE_MONTH = 1;
    private final Integer THREE_MONTHS = 3;
    private final String SINGLE_BAGS = "SINGLE-BAGS";
    private final String MULTIPLE_BAGS = "MULTIPLE-BAGS";

    protected CSVWriterServiceImpl(OrderService orderService, PharmacistOrderService pharmacistOrderService,
                                BlendIngredientService blendIngredientService, EmailService emailService,
                                IngredientUnitService ingredientUnitService) {
        this.orderService = orderService;
        this.pharmacistOrderService = pharmacistOrderService;
        this.blendIngredientService = blendIngredientService;
        this.emailService = emailService;
        this.ingredientUnitService = ingredientUnitService;
    }

    @Override
    public Try<List<PharmacistOrderLine>> createPharmacistRequest() {
        return createDirectory()
                .flatMap(__ -> orderService.findByStatus(OrderStatus.PACKING_SLIP_READY))
                .peek(orders -> LOGGER.info("Orders: {}", orders.size()))
                .map(this::buildPharmacistOrderLines)
                .peek(this::filterOneMonthPharmacistOrders)
                .peek(this::filterThreeMonthsPharmacistOrders)
                .peek(__ -> emailResultsToPharmacist())
                .onFailure(rollbackTransaction())
                .onFailure(peekException());
    }

    private List<PharmacistOrderLine> buildPharmacistOrderLines(List<Order> orders) {
        return orders.stream()
                .map(this::buildPharmacistOrderLines)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private Try<Void> filterOneMonthPharmacistOrders(List<PharmacistOrderLine> pharmacistOrderLineList) {
        Map<Boolean, List<PharmacistOrderLine>> filteredOrdersRecurringMonths = filterRecurringMonths(pharmacistOrderLineList);

        // filters
        List<PharmacistOrderLine> ordersOneMonth = filteredOrdersRecurringMonths.get(true);
        Map<Boolean, List<PharmacistOrderLine>> filteredSequenceTypeOneMonth = filterSequenceType(ordersOneMonth);
        List<PharmacistOrderLine> ordersOneMonthFirst = filteredSequenceTypeOneMonth.get(true);
        List<PharmacistOrderLine> ordersOneMonthRecurring = filteredSequenceTypeOneMonth.get(false);

        List<PharmacistOrderLine> ordersOneMonthFirstSingleBags = filterSingleBag(ordersOneMonthFirst).get(true);
        List<PharmacistOrderLine> ordersOneMonthFirstMultipleBags = filterSingleBag(ordersOneMonthFirst).get(false);

        List<PharmacistOrderLine> ordersOneMonthRecurringSingleBags = filterSingleBag(ordersOneMonthRecurring).get(true);
        List<PharmacistOrderLine> ordersOneMonthRecurringMultipleBags = filterSingleBag(ordersOneMonthRecurring).get(false);

        // results
        List<PharmacistOrderLine> ordersOneMonthFirstSingleBagsSleep = filterSleep(ordersOneMonthFirstSingleBags).get(true);
        List<PharmacistOrderLine> ordersOneMonthFirstSingleBagsNoSleep = filterSleep(ordersOneMonthFirstSingleBags).get(false);

        List<PharmacistOrderLine> ordersOneMonthFirstMultipleBagsSleep = filterSleep(ordersOneMonthFirstMultipleBags).get(true);
        List<PharmacistOrderLine> ordersOneMonthFirstMultipleBagsNoSleep = filterSleep(ordersOneMonthFirstMultipleBags).get(false);

        List<PharmacistOrderLine> ordersOneMonthRecurringSingleBagsSleep = filterSleep(ordersOneMonthRecurringSingleBags).get(true);
        List<PharmacistOrderLine> ordersOneMonthRecurringSingleBagsNoSleep = filterSleep(ordersOneMonthRecurringSingleBags).get(false);

        List<PharmacistOrderLine> ordersOneMonthRecurringMultipleBagsSleep = filterSleep(ordersOneMonthRecurringMultipleBags).get(true);
        List<PharmacistOrderLine> ordersOneMonthRecurringMultipleBagsNoSleep = filterSleep(ordersOneMonthRecurringMultipleBags).get(false);

        return Try.run(() -> processOrderResults(ONE_MONTH, ordersOneMonthFirstSingleBagsSleep, ordersOneMonthFirstSingleBagsNoSleep,
                ordersOneMonthFirstMultipleBagsSleep, ordersOneMonthFirstMultipleBagsNoSleep,
                ordersOneMonthRecurringSingleBagsSleep, ordersOneMonthRecurringSingleBagsNoSleep,
                ordersOneMonthRecurringMultipleBagsSleep, ordersOneMonthRecurringMultipleBagsNoSleep));
    }

    private Try<Void> filterThreeMonthsPharmacistOrders(List<PharmacistOrderLine> pharmacistOrderLineList) {
        Map<Boolean, List<PharmacistOrderLine>> filteredOrdersRecurringMonths = filterRecurringMonths(pharmacistOrderLineList);

        // filters
        List<PharmacistOrderLine> ordersThreeMonths = filteredOrdersRecurringMonths.get(false);
        Map<Boolean, List<PharmacistOrderLine>> filteredSequenceTypeThreeMonths = filterSequenceType(ordersThreeMonths);
        List<PharmacistOrderLine> ordersThreeMonthsFirst = filteredSequenceTypeThreeMonths.get(true);
        List<PharmacistOrderLine> ordersThreeMonthsRecurring = filteredSequenceTypeThreeMonths.get(false);

        List<PharmacistOrderLine> ordersThreeMonthsFirstSingleBags = filterSingleBag(ordersThreeMonthsFirst).get(true);
        List<PharmacistOrderLine> ordersThreeMonthsFirstMultipleBags = filterSingleBag(ordersThreeMonthsFirst).get(false);

        List<PharmacistOrderLine> ordersThreeMonthsRecurringSingleBags = filterSingleBag(ordersThreeMonthsRecurring).get(true);
        List<PharmacistOrderLine> ordersThreeMonthsRecurringMultipleBags = filterSingleBag(ordersThreeMonthsRecurring).get(false);

        // results
        List<PharmacistOrderLine> ordersThreeMonthsFirstSingleBagsSleep = filterSleep(ordersThreeMonthsFirstSingleBags).get(true);
        List<PharmacistOrderLine> ordersThreeMonthsFirstSingleBagsNoSleep = filterSleep(ordersThreeMonthsFirstSingleBags).get(false);

        List<PharmacistOrderLine> ordersThreeMonthsFirstMultipleBagsSleep = filterSleep(ordersThreeMonthsFirstMultipleBags).get(true);
        List<PharmacistOrderLine> ordersThreeMonthsFirstMultipleBagsNoSleep = filterSleep(ordersThreeMonthsFirstMultipleBags).get(false);

        List<PharmacistOrderLine> ordersThreeMonthsRecurringSingleBagsSleep = filterSleep(ordersThreeMonthsRecurringSingleBags).get(true);
        List<PharmacistOrderLine> ordersThreeMonthsRecurringSingleBagsNoSleep = filterSleep(ordersThreeMonthsRecurringSingleBags).get(false);

        List<PharmacistOrderLine> ordersThreeMonthsRecurringMultipleBagsSleep = filterSleep(ordersThreeMonthsRecurringMultipleBags).get(true);
        List<PharmacistOrderLine> ordersThreeMonthsRecurringMultipleBagsNoSleep = filterSleep(ordersThreeMonthsRecurringMultipleBags).get(false);

        return Try.run(() -> processOrderResults(THREE_MONTHS, ordersThreeMonthsFirstSingleBagsSleep, ordersThreeMonthsFirstSingleBagsNoSleep,
                ordersThreeMonthsFirstMultipleBagsSleep, ordersThreeMonthsFirstMultipleBagsNoSleep,
                ordersThreeMonthsRecurringSingleBagsSleep, ordersThreeMonthsRecurringSingleBagsNoSleep,
                ordersThreeMonthsRecurringMultipleBagsSleep, ordersThreeMonthsRecurringMultipleBagsNoSleep));
    }

    private void processOrderResults(
            Integer recurringMonths, List<PharmacistOrderLine> ordersFirstSingleBagsSleep,
            List<PharmacistOrderLine> ordersFirstSingleBagsNoSleep, List<PharmacistOrderLine> ordersFirstMultipleBagsSleep,
            List<PharmacistOrderLine> ordersFirstMultipleBagsNoSleep, List<PharmacistOrderLine> ordersRecurringSingleBagsSleep,
            List<PharmacistOrderLine> ordersRecurringSingleBagsNoSleep, List<PharmacistOrderLine> ordersRecurringMultipleBagsSleep,
            List<PharmacistOrderLine> ordersRecurringMultipleBagsNoSleep) {

        if (THREE_MONTHS.equals(recurringMonths)) {
            processOrdersByLimit(ordersFirstSingleBagsSleep, recurringMonths, SequenceType.FIRST, SINGLE_BAGS, true);
            processOrdersByLimit(ordersFirstSingleBagsNoSleep, recurringMonths, SequenceType.FIRST, SINGLE_BAGS, false);
            processOrdersByLimit(ordersRecurringSingleBagsSleep, recurringMonths, SequenceType.RECURRING, SINGLE_BAGS, true);
            processOrdersByLimit(ordersRecurringSingleBagsNoSleep, recurringMonths, SequenceType.RECURRING, SINGLE_BAGS, false);

            List<PharmacistOrderLine> allThreeMonthsMultipleBags = Stream.of(ordersFirstMultipleBagsSleep, ordersFirstMultipleBagsNoSleep, ordersRecurringMultipleBagsSleep, ordersRecurringMultipleBagsNoSleep)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            processOrdersByLimit(allThreeMonthsMultipleBags, THREE_MONTHS, SequenceType.RECURRING, MULTIPLE_BAGS, true);
        } else {
            processOrdersByLimit(ordersFirstSingleBagsSleep, recurringMonths, SequenceType.FIRST, SINGLE_BAGS, true);
            processOrdersByLimit(ordersFirstSingleBagsNoSleep, recurringMonths, SequenceType.FIRST, SINGLE_BAGS, false);
            processOrdersByLimit(ordersFirstMultipleBagsSleep, recurringMonths, SequenceType.FIRST, MULTIPLE_BAGS, true);
            processOrdersByLimit(ordersFirstMultipleBagsNoSleep, recurringMonths, SequenceType.FIRST, MULTIPLE_BAGS, false);
            processOrdersByLimit(ordersRecurringSingleBagsSleep, recurringMonths, SequenceType.RECURRING, SINGLE_BAGS, true);
            processOrdersByLimit(ordersRecurringSingleBagsNoSleep, recurringMonths, SequenceType.RECURRING, SINGLE_BAGS, false);
            processOrdersByLimit(ordersRecurringMultipleBagsSleep, recurringMonths, SequenceType.RECURRING, MULTIPLE_BAGS, true);
            processOrdersByLimit(ordersRecurringMultipleBagsNoSleep, recurringMonths, SequenceType.RECURRING, MULTIPLE_BAGS, false);
        }
    }

    private void processOrdersByLimit(List<PharmacistOrderLine> pharmacistOrderLineList, Integer recurringMonths, SequenceType sequenceType, String amountOfBagsCode, boolean isSleep) {
        final int limit = recurringMonths.equals(ONE_MONTH) ? 80 : 36;
        int batchNumber = 1;
        List<PharmacistOrderLine> tempPharmacistOrderLineList = new ArrayList<>();

        for (PharmacistOrderLine pharmacistOrderLine: pharmacistOrderLineList) {
            final long amountOfOrders = tempPharmacistOrderLineList.stream()
                    .map(PharmacistOrderLine::getOrderNumber)
                    .distinct()
                    .count();

            if (amountOfOrders >= limit && orderNumberNotInList(pharmacistOrderLine, tempPharmacistOrderLineList)){
                convertAndSaveCsv(recurringMonths, sequenceType, amountOfBagsCode, isSleep, batchNumber, tempPharmacistOrderLineList);
                tempPharmacistOrderLineList.clear();
                batchNumber += 1;
            }
            tempPharmacistOrderLineList.add(pharmacistOrderLine);
        }
        if (!tempPharmacistOrderLineList.isEmpty()) {
            convertAndSaveCsv(recurringMonths, sequenceType, amountOfBagsCode, isSleep, batchNumber, tempPharmacistOrderLineList);
        }
    }

    private boolean orderNumberNotInList(PharmacistOrderLine pharmacistOrderLine, List<PharmacistOrderLine> pharmacistOrderLineList) {
        return pharmacistOrderLineList.stream().noneMatch(pharmacistOrderLine1 -> pharmacistOrderLine.getOrderNumber().equals(pharmacistOrderLine1.getOrderNumber()));
    }

    @NotNull
    private String getOrderNumber(Integer recurringMonths, String amountOfBagsCode) {
        final Long pharmacistOrderLastId = pharmacistOrderService.findByLastInsertedId()
                .map(PharmacistOrder::getId)
                .map(aLong -> aLong+=1)
                .getOrElse(() -> 1L);
        final String DOUBLE = THREE_MONTHS.equals(recurringMonths) && MULTIPLE_BAGS.equals(amountOfBagsCode) ? "DUBBEL" : "";
        final String todayDateFormatted = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
        return "O".concat(todayDateFormatted).concat(String.valueOf(pharmacistOrderLastId)).concat(DOUBLE);
    }

    private Try<Void> createDirectory() {
        return Try.run(() -> Files.createDirectories(Paths.get(CSV_FOLDER)));
    }

    private String getBatchName(Integer recurringMonths, SequenceType sequenceType, String amountOfBagsCode, boolean isSleep, int batchNumber) {
        final String recurringMonthsCode = recurringMonths.equals(ONE_MONTH) ? "ONE-MONTH" : "THREE-MONTHS";
        final String sleepCode = isSleep ? "SLEEP" : "NO-SLEEP";
        return recurringMonthsCode
                .concat("-").concat(String.valueOf(sequenceType))
                .concat("-").concat(amountOfBagsCode)
                .concat("-").concat(sleepCode)
                .concat("-").concat(String.valueOf(batchNumber));
    }

    private Map<Boolean, List<PharmacistOrderLine>> filterRecurringMonths(List<PharmacistOrderLine> pharmacistOrderLineList) {
        return pharmacistOrderLineList.stream()
                .collect(Collectors.partitioningBy(pharmacistOrderLine -> pharmacistOrderLine.getRecurringMonths().equals(ONE_MONTH)));
    }

    private Map<Boolean, List<PharmacistOrderLine>> filterSequenceType(List<PharmacistOrderLine> pharmacistOrderLineList) {
        return pharmacistOrderLineList.stream()
                .collect(Collectors.partitioningBy(pharmacistOrderLine -> pharmacistOrderLine.getSequenceType().equals(SequenceType.FIRST)));
    }

    private Map<Boolean, List<PharmacistOrderLine>> filterSingleBag(List<PharmacistOrderLine> pharmacistOrderLineList) {
        final Long PHARMACIST_CODE_SLEEP = getPharmacistCodeSleep();
        List<PharmacistOrderLine> pharmacistOrderSingleBags = new ArrayList<>();
        List<PharmacistOrderLine> pharmacistOrderDoubleBags = new ArrayList<>();

        Map<String, BigDecimal> customerSumIngredientUnitMap = pharmacistOrderLineList.stream()
                .collect(Collectors.groupingBy(PharmacistOrderLine::getOrderNumber, Collectors.reducing(BigDecimal.ZERO, PharmacistOrderLine::getPharmacistIngredientUnit, BigDecimal::add)));

        // filter sleep because pharmacist does not count it for amount of bags
        Map<String, Integer> customerAmountOfIngredientsMap = pharmacistOrderLineList.stream()
                .filter(pharmacistOrderLine -> !PHARMACIST_CODE_SLEEP.equals(pharmacistOrderLine.getPharmacistIngredientCode()))
                .collect(Collectors.groupingBy(PharmacistOrderLine::getOrderNumber, Collectors.summingInt(PharmacistOrderLine::getPieces)));

        for (PharmacistOrderLine pharmacistOrderLine: pharmacistOrderLineList) {
            BigDecimal sumIngredientUnit = customerSumIngredientUnitMap.get(pharmacistOrderLine.getOrderNumber());
            Integer amountOfIngredients = customerAmountOfIngredientsMap.get(pharmacistOrderLine.getOrderNumber());
            if (Optional.ofNullable(amountOfIngredients).isEmpty() || sumIngredientUnit.doubleValue() <= 101 && amountOfIngredients <= 6) {
                pharmacistOrderSingleBags.add(pharmacistOrderLine);
            } else {
                pharmacistOrderDoubleBags.add(pharmacistOrderLine);
            }

        }
        Map<Boolean, List<PharmacistOrderLine>> filteredSingleBags = new HashMap<>();
        filteredSingleBags.put(true, pharmacistOrderSingleBags);
        filteredSingleBags.put(false, pharmacistOrderDoubleBags);

        return filteredSingleBags;
    }

    private Long getPharmacistCodeSleep() {
        return ingredientUnitService.findAllUnits().map(ingredientUnits -> ingredientUnits.stream()
                .filter(ingredientUnit -> ingredientUnit.getIngredientId().equals(SLEEP_INGREDIENT_ID))
                .findFirst())
                .flatMap(enforceEntityToBePresentTry())
                .map(IngredientUnit::getPharmacistCode)
                .getOrElse(() -> 0L);
    }

    private Map<Boolean, List<PharmacistOrderLine>> filterSleep(List<PharmacistOrderLine> pharmacistOrderLineList) {
        final Long PHARMACIST_CODE_SLEEP = getPharmacistCodeSleep();
        List<PharmacistOrderLine> pharmacistOrderSleep = new ArrayList<>();
        List<PharmacistOrderLine> pharmacistOrderNoSleep = new ArrayList<>();

        final Map<String, List<Long>> customerIngredientsMap = pharmacistOrderLineList.stream()
                .collect(Collectors.groupingBy(PharmacistOrderLine::getOrderNumber, Collectors.mapping(PharmacistOrderLine::getPharmacistIngredientCode, Collectors.toList())));

        for (PharmacistOrderLine pharmacistOrderLine: pharmacistOrderLineList) {
            if (customerIngredientsMap.get(pharmacistOrderLine.getOrderNumber()).contains(PHARMACIST_CODE_SLEEP)) {
                pharmacistOrderSleep.add(pharmacistOrderLine);
            } else {
                pharmacistOrderNoSleep.add(pharmacistOrderLine);
            }
        }

        Map<Boolean, List<PharmacistOrderLine>> filteredSleep = new HashMap<>();
        filteredSleep.put(true, pharmacistOrderSleep);
        filteredSleep.put(false, pharmacistOrderNoSleep);
        return filteredSleep;
    }

    private List<PharmacistOrderLine> buildPharmacistOrderLines(Order order) {
        return ingredientUnitService.findAllUnits()
                .map(ingredientUnits -> ingredientUnits.stream()
                        .filter(ingredientUnit -> blendIngredientService.findByBlendId(order.getBlendId())
                                .map(blendIngredients -> isNoEmptyBlend(blendIngredients, order))
                                .filter(blendIngredients -> !blendIngredients.isEmpty())
                                .map(blendIngredients -> blendIngredients.stream()
                                        .filter(blendIngredient -> !UnitCode.UNITLESS.equals(blendIngredient.getIsUnit()))
                                        .map(BlendIngredient::getIngredientId))
                                .map(longStream -> longStream.anyMatch(aLong -> aLong.equals(ingredientUnit.getIngredientId())))
                                .getOrElse(false)
                        )
                ).map(ingredientUnitStream -> ingredientUnitStream
                        .map(ingredientUnit -> buildPharmacistOrderLine(order, ingredientUnit))
                        .collect(Collectors.toList()))
                .get();
    }

    private List<BlendIngredient> isNoEmptyBlend(List<BlendIngredient> blendIngredients, Order order) {
        if (blendIngredients.isEmpty()) {
            orderService.update(buildOrderWithStatus(order, OrderStatus.ERROR_EMPTY_BLEND));
        }
        return blendIngredients;
    }

    private PharmacistOrderLine buildPharmacistOrderLine(Order order, IngredientUnit ingredientUnit) {
        return new PharmacistOrderLine(
                order.getId(), order.getOrderNumber(), order.getShipToEmail(), order.getShipToFirstName(),
                order.getShipToLastName(), order.getShipToPostalCode(), order.getShipToCity(),
                order.getRecurringMonths(), order.getSequenceType(), null,
                ingredientUnit.getPharmacistCode(), ingredientUnit.getPharmacistUnit(), 1);
    }

    private PharmacistOrderLine buildPharmacistOrderLineWithOrderNumber(PharmacistOrderLine pharmacistOrderLine, String orderNumber) {
        return new PharmacistOrderLine(
                pharmacistOrderLine.getOrderId(), pharmacistOrderLine.getOrderNumber(), pharmacistOrderLine.getEmail(),
                pharmacistOrderLine.getFirstName(), pharmacistOrderLine.getLastName(), pharmacistOrderLine.getPostcode(),
                pharmacistOrderLine.getCity(), pharmacistOrderLine.getRecurringMonths(),
                pharmacistOrderLine.getSequenceType(), orderNumber, pharmacistOrderLine.getPharmacistIngredientCode(),
                pharmacistOrderLine.getPharmacistIngredientUnit(), pharmacistOrderLine.getPieces());
    }

    private void convertAndSaveCsv(Integer recurringMonths, SequenceType sequenceType, String amountOfBagsCode, boolean isSleep, Integer batchNumber, List<PharmacistOrderLine> pharmacistOrderLineList) {
        Try.run(() -> {
            final String orderNumber = getOrderNumber(recurringMonths, amountOfBagsCode);
            final String fileName = orderNumber.concat(CSV_FILE_EXTENSION);
            final String name = getBatchName(recurringMonths, sequenceType, amountOfBagsCode, isSleep, batchNumber);
            final File csvFile = new File(CSV_FOLDER + "/" + fileName);
            try (PrintWriter pw = new PrintWriter(csvFile)) {
                pw.println(convertToSemicolonString(buildColumnArray()));
                pharmacistOrderService.save(buildPharmacistOrder(name, batchNumber, orderNumber, fileName))
                        .peek(pharmacistOrder -> pharmacistOrderLineList.stream().map(pharmacistOrderLine -> new Tuple2<>(pharmacistOrderLine, convertToSemicolonString(buildStringArray(buildPharmacistOrderLineWithOrderNumber(pharmacistOrderLine, orderNumber)))))
                                .forEach(pharmacistOrderLineCommaStringTuple -> Try.run(() -> pw.println(pharmacistOrderLineCommaStringTuple._2))
                                        .flatMap(__ -> orderService.find(pharmacistOrderLineCommaStringTuple._1.getOrderId()))
                                        .peek(order -> orderService.update(buildOrderWithPharmacistOrderNumber(order, pharmacistOrder.getOrderNumber())))
                                )
                        );
            }
        }).onFailure(peekException());
    }

    private void emailResultsToPharmacist() {
        pharmacistOrderService.findByStatus(PharmacistOrderStatus.CREATED)
                .map(pharmacistOrders -> pharmacistOrders.stream()
                        .map(pharmacistOrder -> new File(CSV_FOLDER + "/" + pharmacistOrder.getFileName()))
                        .collect(Collectors.toList()))
                .flatMap(emailService::sendPharmacistRequest)
                .peek(files -> files.forEach(file -> pharmacistOrderService.find(file.getName())
                        .flatMap(pharmacistOrder -> pharmacistOrderService.save(buildPharmacistOrderWithStatus(pharmacistOrder, PharmacistOrderStatus.EMAIL_SEND)))
                        .flatMap(pharmacistOrder -> orderService.findByPharmacistOrderNumber(pharmacistOrder.getOrderNumber()))
                        .peek(orders -> orders.forEach(order -> orderService.update(buildOrderWithStatus(order, OrderStatus.SHIPPED_TO_PHARMACIST))))
                        .onFailure(peekException())
                        .onFailure(rollbackTransaction()))
                );
    }

    private PharmacistOrder buildPharmacistOrder(String batchName, Integer batchNumber, String orderNumber, String fileName) {
        return new PharmacistOrder(null, batchName, batchNumber, orderNumber, fileName, PharmacistOrderStatus.CREATED, LocalDateTime.now(), LocalDateTime.now());
    }

    private PharmacistOrder buildPharmacistOrderWithStatus(PharmacistOrder pharmacistOrder, PharmacistOrderStatus pharmacistOrderStatus) {
        return new PharmacistOrder(
                pharmacistOrder.getId(), pharmacistOrder.getBatchName(), pharmacistOrder.getBatchNumber(),
                pharmacistOrder.getOrderNumber(), pharmacistOrder.getFileName(), pharmacistOrderStatus,
                pharmacistOrder.getCreationTimestamp(), LocalDateTime.now());
    }

    private String[] buildColumnArray() {
        return new String[] { "Customer_id", "Email", "Voornaam", "Achternaam", "Postcode", "Plaats", "aantal", "Ordernummer", "Vitamines", "Stuks" };
    }

    private String[] buildStringArray(PharmacistOrderLine pharmacistOrderLine) {
        return new String[] { pharmacistOrderLine.getOrderNumber(), pharmacistOrderLine.getEmail(),
                pharmacistOrderLine.getFirstName(), pharmacistOrderLine.getLastName(), pharmacistOrderLine.getPostcode(),
                pharmacistOrderLine.getCity(), String.valueOf(pharmacistOrderLine.getRecurringMonths()),
                pharmacistOrderLine.getPharmacistOrderNumber(),
                String.valueOf(pharmacistOrderLine.getPharmacistIngredientCode()),
                String.valueOf(pharmacistOrderLine.getPieces()) };
    }

    private Order buildOrderWithStatus(Order order, OrderStatus orderStatus) {
        return new Order(order.getId(), order.getExternalReference(), order.getOrderNumber(), order.getPaymentId(),
                order.getSequenceType(), order.getPaymentPlanId(), order.getBlendId(), order.getCustomerId(),
                order.getRecurringMonths(), order.getShipToFirstName(), order.getShipToLastName(),
                order.getShipToStreet(), order.getShipToHouseNo(), order.getShipToAnnex(), order.getShipToPostalCode(),
                order.getShipToCity(), order.getShipToCountryCode(), order.getShipToPhone(), order.getShipToEmail(),
                order.getReferralCode(), order.getTrackTraceCode(), order.getPharmacistOrderNumber(), orderStatus,
                order.getCreated(), order.getShipped(), order.getLastModified());
    }

    private Order buildOrderWithPharmacistOrderNumber(Order order, String pharmacistOrderNumber) {
        return new Order(order.getId(), order.getExternalReference(), order.getOrderNumber(), order.getPaymentId(),
                order.getSequenceType(), order.getPaymentPlanId(), order.getBlendId(), order.getCustomerId(),
                order.getRecurringMonths(), order.getShipToFirstName(), order.getShipToLastName(),
                order.getShipToStreet(), order.getShipToHouseNo(), order.getShipToAnnex(), order.getShipToPostalCode(),
                order.getShipToCity(), order.getShipToCountryCode(), order.getShipToPhone(), order.getShipToEmail(),
                order.getReferralCode(), order.getTrackTraceCode(), pharmacistOrderNumber, order.getStatus(),
                order.getCreated(), order.getShipped(), order.getLastModified());
    }

    private String convertToSemicolonString(String[] data) {
        return Stream.of(data)
                .collect(Collectors.joining(";"));
    }

    private <T> Function<Optional<T>, Try<T>> enforceEntityToBePresentTry() {
        return optional -> optional
                .map(entity -> Try.of(() -> entity))
                .orElseGet(() -> Try.failure(new NoSuchElementException()));
    }

    private Consumer<Throwable> rollbackTransaction() {
        return throwable -> TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}

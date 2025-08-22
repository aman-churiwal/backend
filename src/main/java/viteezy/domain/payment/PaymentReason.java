package viteezy.domain.payment;

public class PaymentReason {
    public static final String IBAN_INCORRECT_UNKNOWN = "AC01";
    public static final String HOLDER_CLOSED_ACCOUNT = "AC04";
    public static final String ACCOUNT_BLOCKED_DIRECT_DEBIT = "AC06";
    public static final String INVALID_MANDATE = "MD01";
    public static final String BANK_REFUSED = "SL01";
    public static final String DELIBERATELY_REVERSED = "MD06";
    public static final String INSUFFICIENT_FUNDS = "AM04";
    public static final String OTHER_REASON = "MS03";
}

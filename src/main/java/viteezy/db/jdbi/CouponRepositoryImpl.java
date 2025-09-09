package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.CouponRepository;
import viteezy.domain.pricing.*;
import viteezy.domain.payment.PaymentPlan;

import java.util.Optional;

public class CouponRepositoryImpl implements CouponRepository {

    private static final String SELECT_ALL_COUPONS = "SELECT * FROM coupons ";
    private static final String SELECT_ALL_COUPONS_USED = "SELECT * FROM coupons_used ";
    private static final String SELECT_ALL_COUPONS_DISCOUNT = "SELECT * FROM coupons_discount ";
    private static final String UPDATE_USED_BASE_QUERY = "" +
            "UPDATE coupons " +
            "SET used = used + 1 ";
    private static final String UPDATE_USED_BY_ID = UPDATE_USED_BASE_QUERY + "WHERE id = :id";

    private static final String INSERT_COUPON_USED_CUSTOMER_PAYMENT_PLAN = "INSERT INTO " +
            "coupons_used (coupon_id, customer_id, payment_plan_id, status) " +
            "VALUES (:couponId, :customerId, :paymentPlanId, :status)";

    private static final String INSERT_COUPONS_DISCOUNT = "INSERT INTO " +
            "coupons_discount (coupon_id, payment_plan_id, month, amount, status) " +
            "VALUES (:couponId, :paymentPlanId, :month, :amount, :status)";

    private static final String UPDATE_COUPONS_USED_STATUS = "UPDATE coupons_used SET status = :status WHERE id = :id ";
    private static final String UPDATE_COUPONS_DISCOUNT_STATUS = "UPDATE coupons_discount SET status = :status WHERE id = :id ";

    private static final String UPDATE_EXPIRED_COUPONS_STATUS = "UPDATE coupons SET is_active = FALSE WHERE end_date < NOW() AND is_active = TRUE";

    private final Jdbi jdbi;

    public CouponRepositoryImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public Try<Coupon> find(Long id) {
        final HandleCallback<Coupon, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_COUPONS + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Coupon.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Coupon> find(String couponCode) {
        final HandleCallback<Coupon, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_COUPONS + "WHERE coupon_code = :couponCode AND is_active = TRUE AND end_date > NOW()")
                .bind("couponCode", couponCode)
                .mapTo(Coupon.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Coupon> used(Coupon coupon) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_USED_BY_ID)
                .bindBean(coupon)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(__ -> find(coupon.getId()));
    }

    @Override
    public Try<CouponUsed> findCouponUsed(Long id) {
        final HandleCallback<CouponUsed, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_COUPONS_USED + "WHERE id = :id")
                .bind("id", id)
                .mapTo(CouponUsed.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<CouponDiscount> findDiscount(Long id) {
        final HandleCallback<CouponDiscount, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_COUPONS_DISCOUNT + "WHERE id = :id")
                .bind("id", id)
                .mapTo(CouponDiscount.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<CouponDiscount>> findDiscountByPaymentPlan(Long paymentPlanId, CouponDiscountStatus status) {
        final HandleCallback<Optional<CouponDiscount>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_COUPONS_DISCOUNT + "WHERE payment_plan_id = :paymentPlanId AND status = :status ORDER BY month ASC LIMIT 1")
                .bind("paymentPlanId", paymentPlanId)
                .bind("status", status)
                .mapTo(CouponDiscount.class)
                .findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<CouponUsed>> findCouponUsedByPaymentPlan(Long paymentPlanId) {
        final HandleCallback<Optional<CouponUsed>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_COUPONS_USED + "WHERE payment_plan_id = :paymentPlanId")
                .bind("paymentPlanId", paymentPlanId)
                .mapTo(CouponUsed.class)
                .findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Integer> insertCouponUsed(Coupon coupon,
                                         PaymentPlan paymentPlan, CouponUsedStatus couponUsedStatus) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_COUPON_USED_CUSTOMER_PAYMENT_PLAN)
                .bind("couponId", coupon.getId())
                .bind("customerId", paymentPlan.customerId())
                .bind("paymentPlanId", paymentPlan.id())
                .bind("status", couponUsedStatus)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<CouponUsed> updateCouponUsedStatus(CouponUsed couponUsed) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_COUPONS_USED_STATUS)
                .bindBean(couponUsed)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(__ -> findCouponUsed(couponUsed.getId()));
    }

    @Override
    public Try<CouponDiscount> save(CouponDiscount couponDiscount) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_COUPONS_DISCOUNT)
                .bindBean(couponDiscount)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::findDiscount);
    }

    @Override
    public Try<CouponDiscount> updateCouponDiscountStatus(CouponDiscount couponDiscount) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_COUPONS_DISCOUNT_STATUS)
                .bindBean(couponDiscount)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(__ -> findDiscount(couponDiscount.getId()));
    }

    @Override
    public Try<Integer> updateStatusOfExpiredCoupons() {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_EXPIRED_COUPONS_STATUS)
                .execute();

        return Try.of(() -> jdbi.withHandle(queryCallback));
    }
}

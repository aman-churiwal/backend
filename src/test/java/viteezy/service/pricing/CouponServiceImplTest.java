package viteezy.service.pricing;

import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viteezy.db.CouponRepository;
import viteezy.domain.pricing.Coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CouponServiceImplTest {

    private static final Long COUPON_ID = 1L;
    private static final String COUPON_CODE = "couponCode";
    private static final Coupon COUPON_DISCOUNT_PERCENTAGE = new Coupon(COUPON_ID, COUPON_CODE,
            LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), BigDecimal.valueOf(0.5), BigDecimal.ZERO, BigDecimal.TEN,
            true, 0, 0, Optional.empty(), null, false, Optional.empty(), LocalDateTime.now());
    private static final Coupon COUPON_DISCOUNT_PERCENTAGE_1_MONTH = new Coupon(COUPON_ID, COUPON_CODE,
            LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), BigDecimal.valueOf(0.5), BigDecimal.ZERO, BigDecimal.TEN,
            true, 0, 0, Optional.of(1), null, false, Optional.empty(), LocalDateTime.now());
    private static final Coupon COUPON_DISCOUNT_PERCENTAGE_3_MONTH = new Coupon(COUPON_ID, COUPON_CODE,
            LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), BigDecimal.valueOf(0.5), BigDecimal.ZERO, BigDecimal.TEN,
            true, 0, 0, Optional.of(3), null, false, Optional.empty(), LocalDateTime.now());
    private static final Coupon COUPON_DISCOUNT_PERCENTAGE_3_MONTH_INGREDIENT_ID = new Coupon(COUPON_ID, COUPON_CODE,
            LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), BigDecimal.valueOf(0.5), BigDecimal.ZERO, BigDecimal.TEN,
            true, 0, 0, Optional.of(3), null, false, Optional.of(1L), LocalDateTime.now());
    private final CouponRepository couponRepository = mock(CouponRepository.class);
    private final ReferralService referralService = mock(ReferralService.class);

    private CouponService couponService;

    @BeforeEach
    void setUp() {
        couponService = new CouponServiceImpl(couponRepository, referralService);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(couponRepository, referralService);

        reset(couponRepository, referralService);
    }

    @Test
    void findById() {
        when(couponRepository.find(COUPON_ID))
                .thenReturn(Try.success(COUPON_DISCOUNT_PERCENTAGE));

        final Try<Coupon> coupon = couponService.find(COUPON_ID);

        assertEquals(coupon, Try.success(COUPON_DISCOUNT_PERCENTAGE));
        verify(couponRepository).find(COUPON_ID);
    }

    @Test
    void findByCouponCode() {
        when(couponRepository.find(COUPON_CODE))
                .thenReturn(Try.success(COUPON_DISCOUNT_PERCENTAGE));

        final Try<Coupon> coupon = couponService.find(COUPON_CODE);

        assertEquals(coupon, Try.success(COUPON_DISCOUNT_PERCENTAGE));
        verify(couponRepository).find(COUPON_CODE);
    }

    @Test
    void findValid() {
        when(couponRepository.find(COUPON_CODE))
                .thenReturn(Try.success(COUPON_DISCOUNT_PERCENTAGE));

        final Try<Coupon> coupon = couponService.findValid(COUPON_CODE, null, null);

        assertEquals(coupon, Try.success(COUPON_DISCOUNT_PERCENTAGE));
        verify(couponRepository).find(COUPON_CODE);
    }


    @Test
    void findValid1Month() {
        when(couponRepository.find(COUPON_CODE))
                .thenReturn(Try.success(COUPON_DISCOUNT_PERCENTAGE_1_MONTH));

        final Try<Coupon> coupon = couponService.findValid(COUPON_CODE, 1, null);

        assertEquals(coupon, Try.success(COUPON_DISCOUNT_PERCENTAGE_1_MONTH));
        verify(couponRepository).find(COUPON_CODE);
    }

    @Test
    void findValid3Month() {
        when(couponRepository.find(COUPON_CODE))
                .thenReturn(Try.success(COUPON_DISCOUNT_PERCENTAGE_3_MONTH));

        final Try<Coupon> coupon = couponService.findValid(COUPON_CODE, 3, null);

        assertEquals(coupon, Try.success(COUPON_DISCOUNT_PERCENTAGE_3_MONTH));
        verify(couponRepository).find(COUPON_CODE);
    }

    @Test
    void findValid3MonthIngredientId() {
        when(couponRepository.find(COUPON_CODE))
                .thenReturn(Try.success(COUPON_DISCOUNT_PERCENTAGE_3_MONTH_INGREDIENT_ID));

        final Try<Coupon> coupon = couponService.findValid(COUPON_CODE, 3, null);

        assertEquals(coupon, Try.success(COUPON_DISCOUNT_PERCENTAGE_3_MONTH_INGREDIENT_ID));
        verify(couponRepository).find(COUPON_CODE);
    }
}
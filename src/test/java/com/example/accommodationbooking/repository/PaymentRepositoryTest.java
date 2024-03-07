package com.example.accommodationbooking.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.accommodationbooking.model.Payment;
import com.example.accommodationbooking.model.enumeration.PaymentStatus;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:database/insert-accommodation.sql",
        "classpath:database/insert-user-payment_repository-test.sql",
        "classpath:database/insert-booking.sql",
        "classpath:database/insert-payment.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class PaymentRepositoryTest {
    private static final Long USER_ID = 1L;
    private static final String SESSION_ID = "session123";
    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("Find bookings by booking user ID, return payments list")
    public void findBookings_ByBookingUserId_returnPaymentsList() {
        List<Payment> actual = paymentRepository.findAllByBookingUserId(USER_ID);
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("Find payment by session ID, return optional payment")
    public void findPayment_BySessionId_returnOptionalPayment() {
        Optional<Payment> bySessionId = paymentRepository.findBySessionId(SESSION_ID);
        assertTrue(bySessionId.isPresent());
    }

    @Test
    @DisplayName("Find bookings by payment status, return optional payment")
    public void findBookings_ByPaymentStatus_returnOptionalPayment() {
        List<Payment> actual = paymentRepository.findAllByPaymentStatus(PaymentStatus.PENDING);
        assertThat(actual).hasSize(1);
    }
}

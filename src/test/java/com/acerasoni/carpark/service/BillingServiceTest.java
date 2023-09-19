package com.acerasoni.carpark.service;

import com.acerasoni.carpark.model.Bill;
import com.acerasoni.carpark.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    private static final long TEST_HOURLY_RATE = 17;
    private static final int TEST_SPEED_UP_FACTOR = 1;

    @Mock
    private FormattingService formattingService;

    private BillingService billingService;
    private Set<Bill> billRegister;

    @BeforeEach
    void setUp() {
        billRegister = new HashSet<>();
        billingService = new BillingService(billRegister, formattingService, TEST_HOURLY_RATE, TEST_SPEED_UP_FACTOR);
    }

    @Test
    void billCar_whenCarIsValid_thenGenerateBillAndAssignToCar() {
        final var departure = Instant.now();
        final var admission = Instant.now().minus(1, ChronoUnit.HOURS);

        final var car = new Car(0L);
        car.setDepartureTime(departure);
        car.setAdmissionTime(admission);

        final var expectedBill = new Bill(TEST_HOURLY_RATE);

        billingService.billCar(car);
        verify(formattingService).formatCurrency(TEST_HOURLY_RATE);
        assertEquals(expectedBill, car.getBill());
        assertEquals(1, billRegister.size());
        assertTrue(billRegister.contains(expectedBill));
    }

    @Test
    void billCar_whenArrivalTimeIsNull_thenThrowException() {
        final var car = new Car(0L);
        car.setDepartureTime(Instant.now());
        assertThrows(NullPointerException.class, () -> billingService.billCar(car));
    }

    @Test
    void billCar_whenDepartureTimeIsNull_thenThrowException() {
        final var car = new Car(0L);
        car.setAdmissionTime(Instant.now());
        assertThrows(NullPointerException.class, () -> billingService.billCar(car));
    }

    @Test
    void generateRevenueReport_whenBillRegisterIsEmpty_thenGenerateEmptyRevenueReport() {
        final var testReport = billingService.generateRevenueReport();
        assertEquals(0L, testReport.numberOfCars());
        verify(formattingService).formatCurrency(0);
    }

    @Test
    void generateRevenueReport_whenBillRegisterIsNotEmpty_thenGenerateNonEmptyRevenueReport() {
        billRegister.add(new Bill(15));
        billRegister.add(new Bill(32));
        billRegister.add(new Bill(95));
        final var testReport = billingService.generateRevenueReport();
        assertEquals(3L, testReport.numberOfCars());
        verify(formattingService).formatCurrency(142);
    }
}
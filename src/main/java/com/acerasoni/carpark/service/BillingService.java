package com.acerasoni.carpark.service;

import com.acerasoni.carpark.model.Bill;
import com.acerasoni.carpark.model.Car;
import com.acerasoni.carpark.model.RevenueReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BillingService {

    private final Set<Bill> billRegister;
    private final FormattingService formattingService;
    private final double millisecondRate;
    private final int speedUpFactor;


    public BillingService(final Set<Bill> billRegister,
                          final FormattingService formattingService,
                          @Value("${carpark.hourly-rate}") final double hourlyRate,
                          @Value("${carpark.speed-up-factor}") final int speedUpFactor) {
        this.billRegister = billRegister;
        this.formattingService = formattingService;
        this.millisecondRate = hourlyRate / TimeUnit.HOURS.toMillis(1);
        this.speedUpFactor = speedUpFactor;
    }

    public void billCar(final Car car) {
        final var departureTime = car.getDepartureTime();
        final var admissionTime = car.getAdmissionTime();
        Objects.requireNonNull(admissionTime);
        Objects.requireNonNull(departureTime);

        final long durationOfStayMillis = Duration.between(admissionTime, departureTime).toMillis();
        final double amountCharged = durationOfStayMillis * millisecondRate * speedUpFactor;

        final var durationOfStayFormatted = String.format("%s minutes (~%s hours)",
                TimeUnit.MILLISECONDS.toMinutes(durationOfStayMillis * speedUpFactor),
                TimeUnit.MILLISECONDS.toHours(durationOfStayMillis * speedUpFactor));

        log.info("Car #{} was charged {} for spending {} in the carpark",
                car.getId(),
                formattingService.formatCurrency(amountCharged),
                durationOfStayFormatted);

        final var bill = new Bill(amountCharged);

        billRegister.add(bill);
        car.setBill(bill);
    }

    public RevenueReport generateRevenueReport() {
        return new RevenueReport(
                billRegister.size(),
                formattingService.formatCurrency(billRegister.parallelStream().mapToDouble(Bill::amount).sum()));
    }
}

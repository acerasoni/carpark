package com.acerasoni.carpark.service;

import com.acerasoni.carpark.model.Bill;
import com.acerasoni.carpark.model.Car;
import com.acerasoni.carpark.util.FormatterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BillingService {

    private final double millisecondRate;
    private final int speedUpFactor;

    public BillingService(@Value("${carpark.hourly-rate}") final double hourlyRate,
                          @Value("${carpark.speed-up-factor}") final int speedUpFactor) {
        this.millisecondRate = hourlyRate / TimeUnit.HOURS.toMillis(1);
        this.speedUpFactor = speedUpFactor;
    }

    public void billCar(final Car car) {
        final var admissionTime = car.getAdmissionTime();
        final var departureTime = car.getDepartureTime();

        Objects.requireNonNull(admissionTime);
        Objects.requireNonNull(departureTime);

        final long durationOfStayMillis = Duration.between(admissionTime, departureTime).toMillis();
        final String amountCharged = FormatterUtil.formatCurrency(durationOfStayMillis * millisecondRate * speedUpFactor);

        final var durationOfStayFormatted = String.format("%s minutes (~%s hours)",
                TimeUnit.MILLISECONDS.toMinutes(durationOfStayMillis * speedUpFactor),
                TimeUnit.MILLISECONDS.toHours(durationOfStayMillis * speedUpFactor));

        log.info("Car #{} was charged {} for spending {} in the carpark",
                car.getId(),
                amountCharged,
                durationOfStayFormatted);
        car.setBill(new Bill(amountCharged, durationOfStayFormatted));
    }
}

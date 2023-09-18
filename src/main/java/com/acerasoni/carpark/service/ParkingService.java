package com.acerasoni.carpark.service;

import com.acerasoni.carpark.exception.CarparkException;
import com.acerasoni.carpark.model.Car;
import com.acerasoni.carpark.util.FormatterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.time.Instant;

@Service
@Slf4j
public class ParkingService {

    private final Sinks.Many<Car> carPark;
    private final String dateFormat;

    public ParkingService(final Sinks.Many<Car> carPark,
                          @Value("${date-format}") final String dateFormat) {
        this.carPark = carPark;
        this.dateFormat = dateFormat;
    }

    public void parkCar(final Car car) {
        switch (carPark.tryEmitNext(car)) {
            case OK -> {
                final var admissionTime = Instant.now();
                car.setAdmissionTime(admissionTime);
                log.debug("Admitted car #{} into the carpark at '{}'.", car.getId(), FormatterUtil.formatInstant(admissionTime, dateFormat));
            }
            case FAIL_OVERFLOW, FAIL_ZERO_SUBSCRIBER ->
                    log.warn("Unable to park car #{}. The carpark is currently full.", car.getId());
            case Sinks.EmitResult emitResult -> throw new CarparkException(
                    String.format("Encountered error %s when attempting to park car #%s", emitResult, car.getId()));
        }
    }
}

package com.acerasoni.carpark.service;

import com.acerasoni.carpark.exception.CarparkException;
import com.acerasoni.carpark.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.time.Instant;

@Service
@Slf4j
public class ParkingService {

    private final Sinks.Many<Car> carPark;
    private final FormattingService formattingService;

    public ParkingService(final Sinks.Many<Car> carPark,
                          final FormattingService formattingService) {
        this.carPark = carPark;
        this.formattingService = formattingService;
    }

    public void parkCar(final Car car) {
        switch (carPark.tryEmitNext(car)) {
            case OK -> {
                final var admissionTime = Instant.now();
                car.setAdmissionTime(admissionTime);
                log.debug("Admitted car #{} into the carpark at '{}'.", car.getId(), formattingService.formatInstant(admissionTime));
            }
            case FAIL_OVERFLOW, FAIL_ZERO_SUBSCRIBER ->
                    log.warn("Unable to park car #{}. The carpark is currently full.", car.getId());
            case Sinks.EmitResult emitResult -> throw new CarparkException(
                    String.format("Encountered unexpected error %s when attempting to park car #%s", emitResult, car.getId()));
        }
    }
}

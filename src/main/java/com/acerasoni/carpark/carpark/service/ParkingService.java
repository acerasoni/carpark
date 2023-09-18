package com.acerasoni.carpark.carpark.service;

import com.acerasoni.carpark.carpark.exception.CarparkException;
import com.acerasoni.carpark.carpark.model.Car;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
@AllArgsConstructor
@Slf4j
public class ParkingService {

    private final Sinks.Many<Car> carPark;

    public void parkCar(final Car car) {
        switch (carPark.tryEmitNext(car)) {
            case OK -> log.debug("Admitted car {} into the carpark.", car.getId());
            case FAIL_OVERFLOW, FAIL_ZERO_SUBSCRIBER ->
                    log.warn("Unable to park car #{}. The carpark is currently full.", car.getId());
            case Sinks.EmitResult emitResult -> throw new CarparkException(
                    String.format("Encountered error %s when attempting to park car #%s", emitResult, car.getId()));
        }
    }
}

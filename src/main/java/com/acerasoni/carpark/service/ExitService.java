package com.acerasoni.carpark.service;

import com.acerasoni.carpark.model.Car;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExitService {

    @Value("${car.departure-delay-millis}")
    private long departureDelay;

    private final FormattingService formattingService;
    private final Sinks.Many<Car> carPark;

    public Flux<Car> generateExitQueue() {
        log.info("Beginning the generation of a car exit queue");
        return carPark
                .asFlux()
                .delayElements(Duration.ofMillis(departureDelay))
                .map((Car car) -> {
                    final var departureTime = Instant.now();
                    car.setDepartureTime(departureTime);
                    log.debug("Car {} departed at '{}'.", car.getId(), formattingService.formatInstant(departureTime));
                    return car;
                });
    }
}

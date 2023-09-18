package com.acerasoni.carpark.carpark.service;

import com.acerasoni.carpark.carpark.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Service
@Slf4j
public class ExitService {

    private final Sinks.Many<Car> carPark;
    private final long departureDelay;

    public ExitService(final Sinks.Many<Car> carPark, @Value("${carpark.departure-delay-millis}") long delay) {
        this.carPark = carPark;
        this.departureDelay = delay;
    }

    public Flux<Car> departCars() {
        return carPark
                .asFlux()
                .delayElements(Duration.ofMillis(departureDelay))
                .publishOn(Schedulers.parallel());
    }
}

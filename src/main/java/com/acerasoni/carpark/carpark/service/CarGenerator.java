package com.acerasoni.carpark.carpark.service;

import com.acerasoni.carpark.carpark.model.Car;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
public class CarGenerator {

    private long delay;

    public CarGenerator(@Value("${carpark.arrival-delay-millis}") long delay) {
        this.delay = delay;
    }

    public Flux<Car> generateCars() {
        return Flux
                .interval(Duration.of(delay, ChronoUnit.MILLIS))
                .map(Car::new);
    }
}

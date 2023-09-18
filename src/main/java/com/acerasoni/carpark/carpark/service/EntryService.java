package com.acerasoni.carpark.carpark.service;

import com.acerasoni.carpark.carpark.model.Car;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Service
public class EntryService {

    private final long arrivalDelay;

    public EntryService(@Value("${carpark.arrival-delay-millis}") long delay) {
        this.arrivalDelay = delay;
    }

    public Flux<Car> generateCars() {
        return Flux
                .interval(Duration.ofMillis(arrivalDelay))
                .publishOn(Schedulers.parallel())
                .map(Car::new);
    }
}

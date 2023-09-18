package com.acerasoni.carpark.service;

import com.acerasoni.carpark.model.Car;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@NoArgsConstructor
@Slf4j
public class EntryService {

    @Value("${car.arrival-delay-millis}")
    private long arrivalDelay;

    public Flux<Car> generateEntryQueue() {
        log.info("Beginning the generation of a car entry queue");
        return Flux
                .interval(Duration.ofMillis(arrivalDelay))
                .map(Car::new);
    }
}

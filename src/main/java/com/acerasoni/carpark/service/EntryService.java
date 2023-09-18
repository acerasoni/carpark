package com.acerasoni.carpark.service;

import com.acerasoni.carpark.model.Car;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@NoArgsConstructor
public class EntryService {

    @Value("${car.arrival-delay-millis}")
    private long arrivalDelay;

    public Flux<Car> generateEntryQueue() {
        return Flux
                .interval(Duration.ofMillis(arrivalDelay))
                .map(Car::new);
    }
}

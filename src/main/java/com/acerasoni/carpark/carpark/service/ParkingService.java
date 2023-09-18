package com.acerasoni.carpark.carpark.service;

import com.acerasoni.carpark.carpark.model.Car;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;

@Service
@AllArgsConstructor
@Slf4j
public class ParkingService {

    private final BlockingQueue<Car> carPark;

    public void parkCar(final Car car) {
        if (carPark.offer(car)) {
            log.debug("Admitted car {} into the carpark.", car.getId());
            car.setAdmissionTime(Instant.now());
        } else {
            log.warn("Unable to park car #{}. The carpark is currently full.", car.getId());
        }
    }
}
package com.acerasoni.carpark.carpark.service;

import com.acerasoni.carpark.carpark.model.Car;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;

@Service
@AllArgsConstructor
@Slf4j
public class ExitService {

    private final BlockingQueue<Car> carPark;

    public Flux<Car> departCars() {
        return Flux.fr
        });
    }
}

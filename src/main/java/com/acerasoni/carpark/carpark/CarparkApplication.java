package com.acerasoni.carpark.carpark;

import com.acerasoni.carpark.carpark.service.CarGenerator;
import com.acerasoni.carpark.carpark.service.ParkingService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.scheduler.Schedulers;

//TODO: Testing https://www.baeldung.com/spring-webflux-backpressure
@SpringBootApplication
@AllArgsConstructor
public class CarparkApplication implements CommandLineRunner {

    private final CarGenerator carArrivalService;
    private final ParkingService parkingService;

    public static void main(final String[] args) {
        SpringApplication.run(CarparkApplication.class, args);
    }

    @Override
    public void run(final String... args) {
//        carArrivalService.simulateCarArrivals().subscribeOn(Schedulers.parallel()).subscribe(this::onEvent);
//
//        Flux.interval(Duration.ofMillis(10))
//                .onBackpressureBuffer(100, aLong ->
//                        System.out.println("Full LOL :  " + aLong), BufferOverflowStrategy.DROP_LATEST)
//                .log()
////                .concatMap(x -> Mono.delay(Duration.ofMillis(100)))
//                .map(id -> new CarArrivalEvent(id, Instant.now()))
////                .blockLast()
//                .subscribe(this::onEvent);

        carArrivalService.generateCars().subscribeOn(Schedulers.parallel()).subscribe(parkingService::parkCar);
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
